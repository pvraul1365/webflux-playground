package com.rperezv.webflux_playground.sec09.config

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import com.rperezv.webflux_playground.sec08.dto.UploadResponse
import com.rperezv.webflux_playground.sec09.service.ProductService
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

    @PostMapping
    fun saveProduct(@RequestBody mono: Mono<ProductDto>): Mono<ProductDto> = service.saveProduct(mono)

    @GetMapping("stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun productStream(): Flux<ProductDto> = service.productStream()


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