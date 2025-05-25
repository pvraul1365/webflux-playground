package com.rperezv.webflux_playground.sec08.repository

import com.rperezv.webflux_playground.sec08.entity.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface ProductRepository : ReactiveMongoRepository<Product, String> {

    fun findByDescription(description: String): Mono<Product>

    fun findByPriceBetween(from: Int, to: Int): Flux<Product>

    fun findAllBy(pageable: Pageable): Flux<Product>

}