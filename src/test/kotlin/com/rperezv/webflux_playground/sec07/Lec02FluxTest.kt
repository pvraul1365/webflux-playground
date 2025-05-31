package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec10.dto.Product
import com.rperezv.webflux_playground.sec07.AbstractWebClient
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.time.Duration

class Lec02FluxTest : AbstractWebClient() {

    private val client: WebClient = createWebClient()

    @Test
    fun streamingResponse() {

        this.client.get()
            .uri("/{lec}/product/stream", "lec02")
            .retrieve()
            .bodyToFlux(Product::class.java)
            .take(Duration.ofSeconds(3))
            .log()
            .then()
            .`as`(StepVerifier::create )
            .expectComplete()
            .verify()

    }

}