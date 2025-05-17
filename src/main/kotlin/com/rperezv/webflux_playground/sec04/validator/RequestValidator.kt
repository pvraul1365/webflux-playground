package com.rperezv.webflux_playground.sec04.validator

import com.rperezv.webflux_playground.sec03.dto.CustomerDto
import com.rperezv.webflux_playground.sec04.exceptions.ApplicationExceptions
import reactor.core.publisher.Mono

object RequestValidator {

    fun validate(): (Mono<CustomerDto>) -> Mono<CustomerDto> {
        return { mono ->
            mono.filter { hasName(it) }
                .switchIfEmpty(ApplicationExceptions.missingName())
                .filter { hasValidEmail(it) }
                .switchIfEmpty(ApplicationExceptions.missingValidEmail())
        }
    }

    fun hasName(dto: CustomerDto): Boolean {
        return dto.name != null
    }

    fun hasValidEmail(dto: CustomerDto): Boolean {
        return dto.email != null && dto.email!!.contains("@")
    }

}