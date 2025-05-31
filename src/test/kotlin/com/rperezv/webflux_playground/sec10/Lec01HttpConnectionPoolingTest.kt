package com.rperezv.webflux_playground.sec10

import com.rperezv.webflux_playground.sec10.AbstractWebClient
import com.rperezv.webflux_playground.sec10.dto.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class Lec01HttpConnectionPoolingTest : AbstractWebClient() {

    private val client: WebClient = super.createWebClient()

    @Test
    fun concurrentRequest() {
        val max = 3
        Flux.range(1, max)
            .flatMap(this::getProduct)
            .collectList()
            .`as`(StepVerifier::create)
            .assertNext { products -> Assertions.assertEquals(max, products.size) }
            .expectComplete()
            .verify()

        Thread.sleep(Duration.ofMinutes(1))
    }

    private fun getProduct(id: Int): Mono<Product> {
        return client.get()
            .uri("/product/{id}", id)
            .retrieve()
            .bodyToMono(Product::class.java)
    }

}