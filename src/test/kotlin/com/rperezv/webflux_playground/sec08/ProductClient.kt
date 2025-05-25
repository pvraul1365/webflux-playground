package com.rperezv.webflux_playground.sec08

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import com.rperezv.webflux_playground.sec08.dto.UploadResponse
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class ProductClient {

    private val client: WebClient = WebClient.builder()
        .baseUrl("http://localhost:8080")
        .build()

    fun uploadProducts(flux: Flux<ProductDto>): Mono<UploadResponse> {
        return client.post()
            .uri("/api/v3/products/upload")
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(flux, ProductDto::class.java)
            .retrieve()
            .bodyToMono(UploadResponse::class.java)
    }

    fun downloadProducts(): Flux<ProductDto> {
        return client.get()
            .uri("/api/v3/products/download")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(ProductDto::class.java)
    }
}