package com.nttdata.BancaProductActive.web;

import com.nttdata.BancaProductActive.domain.ProductActive;
import com.nttdata.BancaProductActive.service.ProductActiveService;
import com.nttdata.BancaProductActive.web.mapper.ProductActiveMapper;
import com.nttdata.BancaProductActive.web.model.ProductActiveModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/productactive")
public class ProductActiveController {
    @Value("${spring.application.name}")
    String name;

    @Value("${server.port}")
    String port;

    @Autowired
    private ProductActiveService productActiveService;

    @Autowired
    private ProductActiveMapper productActiveMapper;

    @GetMapping()
    public Mono<ResponseEntity<Flux<ProductActiveModel>>> getAll(){
        log.info("getAll executed");
        return Mono.just(ResponseEntity.ok()
                .body(productActiveService.findAll()
                        .map(productActive -> productActiveMapper.entityToModel(productActive))));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductActiveModel>> getById(@PathVariable String id){
        log.info("getById executed {}", id);
        Mono<ProductActive> response = productActiveService.findById(id);
        return response
                .map(productActive -> productActiveMapper.entityToModel(productActive))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<ProductActiveModel>> create(@Valid @RequestBody ProductActiveModel request){
        log.info("create executed {}", request);
        return productActiveService.create(productActiveMapper.modelToEntity(request))
                .map(productActive -> productActiveMapper.entityToModel(productActive))
                .flatMap(c -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "productActive", c.getIdProductActive())))
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductActiveModel>> updateById(@PathVariable String id, @Valid @RequestBody ProductActiveModel request){
        log.info("updateById executed {}:{}", id, request);
        return productActiveService.update(id, productActiveMapper.modelToEntity(request))
                .map(productActive -> productActiveMapper.entityToModel(productActive))
                .flatMap(c -> Mono.just(ResponseEntity.created(URI.create(String.format("http://%s:%s/%s/%s", name, port, "productActive", c.getIdProductActive())))
                        .body(c)))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id){
        log.info("deleteById executed {}", id);
        return productActiveService.delete(id)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
