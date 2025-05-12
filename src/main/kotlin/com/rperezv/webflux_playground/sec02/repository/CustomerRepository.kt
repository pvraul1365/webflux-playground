package com.rperezv.webflux_playground.sec02.repository

import com.rperezv.webflux_playground.sec02.domain.Customer
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CustomerRepository : ReactiveMongoRepository<Customer, String> {

    fun findByEmail(email: String): Mono<Customer>

    fun findByName(name: String): Flux<Customer>

}