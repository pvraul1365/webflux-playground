package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec01.Product
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.util.Map

class Lec04HeaderTest : AbstractWebClient() {

    private val client: WebClient = super.createWebClient { b -> b.defaultHeader("caller-id", "order-service") }

    @Test
    fun defaultHeader() {

        this.client.get()
            .uri("/{lec}/product/{id}", "lec04", 1)
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()

    }

    @Test
    fun overrideHeader() {

        this.client.get()
            .uri("/{lec}/product/{id}", "lec04", 1)
            .header("caller-id", "new-value")
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()

    }

    @Test
    fun headersWithMap() {
        val map = Map.of(
            "caller-id", "new-value",
            "some-key", "some-value"
        )

        this.client.get()
            .uri("/{lec}/product/{id}", "lec04", 1)
            .headers { it.setAll(map) }
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()

    }

}