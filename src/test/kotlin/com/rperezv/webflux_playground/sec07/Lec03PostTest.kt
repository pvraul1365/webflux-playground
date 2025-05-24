package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec07.dto.Product
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration


class Lec03PostTest : AbstractWebClient() {

    private val client: WebClient = createWebClient()

    @Test
    fun postBodyValue() {

        val product = Product(null, "iphone", 1000)

        this.client.post()
            .uri("/{lec}/product", "lec03")
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create )
            .expectComplete()
            .verify()

    }

    @Test
    fun postBody() {

        val monoProduct = Mono.fromSupplier { Product(null, "iphone", 1000) }
            .delayElement(Duration.ofSeconds(1))

        this.client.post()
            .uri("/{lec}/product", "lec03")
            .body(monoProduct, Product::class.java)
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create )
            .expectComplete()
            .verify()

    }

}