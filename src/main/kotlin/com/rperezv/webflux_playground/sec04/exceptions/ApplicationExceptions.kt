package com.rperezv.webflux_playground.sec04.exceptions

import reactor.core.publisher.Mono

object ApplicationExceptions {

    fun <T> customerNotFound(id: String): Mono<T> {
        return Mono.error(CustomerNotFoundException(id))
    }

    fun <T> missingName(): Mono<T> {
        return Mono.error(InvalidInputException("Name is requiered"))
    }

    fun <T> missingValidEmail(): Mono<T> {
        return Mono.error(InvalidInputException("Valid email is requiered"))
    }

}