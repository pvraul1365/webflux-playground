package com.rperezv.webflux_playground.sec08.controller

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import com.rperezv.webflux_playground.sec08.dto.UploadResponse
import com.rperezv.webflux_playground.sec08.service.ProductService
import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
@RequestMapping("api/v3/products")
class ProductController(
    private val service: ProductService
) {

    companion object : KLogging()

    @PostMapping("upload", consumes = [MediaType.APPLICATION_NDJSON_VALUE])
    fun uploadProducts(@RequestBody flux: Flux<ProductDto>) : Mono<UploadResponse> {
        logger.info("\uD83D\uDE80 - UploadProducts (in controller) was called!")

        return service.saveProducts(flux.doOnNext { dto -> logger.info { "received: $dto" } }
            ).then(service.getProductsCount())
            .map { count -> UploadResponse(UUID.randomUUID(), count) }
    }

    @GetMapping("download", produces = [MediaType.APPLICATION_NDJSON_VALUE])
    fun downloadProducts(): Flux<ProductDto> = service.allProducts()

}