package com.rperezv.webflux_playground.sec01

import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
@RequestMapping("reactive")
class ReactiveWebController {

    companion object : KLogging()

    val webClient: WebClient = WebClient.builder()
        .baseUrl("http://localhost:7070")
        .build()

    @GetMapping("products")
    fun getProducts(): Flux<Product> {
        return this.webClient.get()
            .uri("/demo01/products/notorious")
            .retrieve()
            .bodyToFlux(Product::class.java)
            .onErrorComplete()
            .doOnNext {
                logger.info("received: $it")
            }

    }

    @GetMapping("products/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getProductsStream(): Flux<Product> {
        return this.webClient.get()
            .uri("/demo01/products")
            .retrieve()
            .bodyToFlux(Product::class.java)
            .doOnNext {
                logger.info("received: $it")
            }

    }

}