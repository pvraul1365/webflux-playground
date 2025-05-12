package com.rperezv.webflux_playground.sec02.repository

import com.rperezv.webflux_playground.sec02.domain.Product
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface ProductRepository : ReactiveMongoRepository<Product, String> {

    fun findByDescription(description: String): Mono<Product>

}