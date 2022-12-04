package com.nttdata.BancaProductActive.repository;

import com.nttdata.BancaProductActive.domain.ProductActive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductActiveRepository extends ReactiveMongoRepository<ProductActive, String> {
    Mono<ProductActive> findByIdentityNumberandTypeCredit(String dni, String typecredito);
}
