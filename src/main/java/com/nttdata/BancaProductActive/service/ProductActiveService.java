package com.nttdata.BancaProductActive.service;

import com.nttdata.BancaProductActive.domain.Client;
import com.nttdata.BancaProductActive.domain.ProductActive;
import com.nttdata.BancaProductActive.repository.ProductActiveRepository;
import com.nttdata.BancaProductActive.web.mapper.ProductActiveMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductActiveService {
    @Autowired
    private ProductActiveRepository productActiveRepository;

    @Autowired
    private ProductActiveMapper productActiveMapper;

    private final String BASE_URL = "http://localhost:9040";

    TcpClient tcpClient = TcpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300)
            .doOnConnected(connection ->
                    connection.addHandlerLast(new ReadTimeoutHandler(3))
                            .addHandlerLast(new WriteTimeoutHandler(3)));

    private final WebClient client = WebClient.builder()
            .baseUrl("http://localhost:9040")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
            .build();


    public Flux<ProductActive> findAll(){
        log.debug("findAll executed");
        return productActiveRepository.findAll();
    }

    public Mono<ProductActive> findById(String productactiveId){
        log.debug("findById executed {}", productactiveId);
        return productActiveRepository.findById(productactiveId);
    }

    public Mono<Client> findBydni(String dni){
        return this.client.get().uri("http://localhost:9040/v1/client/findByDni/{dni}", dni)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Client.class);
    }

    public Mono<ProductActive> create(ProductActive productActive){
        log.debug("create executed {}", productActive);

        Mono<Client> client = findBydni(productActive.getIdentityNumber());
        log.debug("findBydni executed {}" , client);
        log.info("findBydni executed {}" , client);
        System.out.println("client " + client);
        Mono<ProductActive> product = productActiveRepository.findByIdentityNumberandTypeCredit(productActive.getIdentityNumber(), productActive.getTypeCredit() );

        return client.switchIfEmpty(Mono.error(new Exception("Client Not Found" + productActive.getIdentityNumber())))
                .flatMap(client1 -> {
                    if(client1.getTypeClient().equals("Empresarial")) {
                        return productActiveRepository.findByIdentityNumberandTypeCredit(productActive.getIdentityNumber(), productActive.getTypeCredit())
                                .flatMap(product1 -> {
                                    if(product1.getTypeCredit().equals("Personal")){
                                        return Mono.error(new Exception("No puede realizar mas de un registro Personal."));
                                    }else{
                                        return productActiveRepository.save(productActive);
                                    }
                                }).switchIfEmpty(productActiveRepository.save(productActive));
                    }
                    else{
                      return Mono.error(new Exception("El producto activo que desea registrar, tiene asociado un DNI de tipo cliente Personal " + client1.getTypeClient()));
                    }
                });

    }


    public Mono<ProductActive> update(String productactiveId,  ProductActive productActive){
        log.debug("update executed {}:{}", productactiveId, productActive);
        return productActiveRepository.findById(productactiveId)
                .flatMap(dbProductoActive -> {
                    productActiveMapper.update(dbProductoActive, productActive);
                    return productActiveRepository.save(dbProductoActive);
                });
    }


    public Mono<ProductActive> delete(String productactiveId){
        log.debug("delete executed {}", productactiveId);
        return productActiveRepository.findById(productactiveId)
                .flatMap(existingProductActive -> productActiveRepository.delete(existingProductActive)
                        .then(Mono.just(existingProductActive)));
    }
}
