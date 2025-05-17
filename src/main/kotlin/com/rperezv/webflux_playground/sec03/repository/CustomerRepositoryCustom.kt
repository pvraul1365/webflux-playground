package com.rperezv.webflux_playground.sec03.repository

import reactor.core.publisher.Mono

interface CustomerRepositoryCustom {
    fun deleteCustomerById(id: String): Mono<Boolean>
}