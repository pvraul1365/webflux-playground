package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec07.dto.Product
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class Lec07BasicAuthTest : AbstractWebClient() {

    private val client: WebClient = super.createWebClient {
        builder -> builder.defaultHeaders { headers -> headers.setBasicAuth("java", "secret") }
    }

    @Test
    fun basicAuth() {
        this.client.get()
            .uri("/{lec}/product/{id}", "lec07", 1)
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()
    }

}