package com.rperezv.webflux_playground.sec10

import com.rperezv.webflux_playground.sec10.dto.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.test.StepVerifier

class Lec01HttpConnectionPoolingTest : AbstractWebClient() {

    private val client: WebClient = super.createWebClient{ b ->
        val poolSize = 501
        val provider = ConnectionProvider.builder("vins")
            .lifo()
            .maxConnections(poolSize)
            .pendingAcquireMaxCount(poolSize * 5)
            .build()

        val httpClient = HttpClient.create(provider)
            .compress(true)
            .keepAlive(true)

        b.clientConnector(ReactorClientHttpConnector(httpClient))
    }

    @Test
    fun concurrentRequest() {
        val max = 501
        Flux.range(1, max)
            .flatMap(this::getProduct, max)
            .collectList()
            .`as`(StepVerifier::create)
            .assertNext { products -> Assertions.assertEquals(max, products.size) }
            .expectComplete()
            .verify()

    }

    private fun getProduct(id: Int): Mono<Product> {
        return client.get()
            .uri("/product/{id}", id)
            .retrieve()
            .bodyToMono(Product::class.java)
    }

}