package com.rperezv.webflux_playground.sec08

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import mu.KLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.nio.file.Path

class ProductsUploadDownloadTest {

    companion object : KLogging()

    private val productClient: ProductClient = ProductClient()

    @Test
    fun upload() {

        val flux = Flux.range(1, 1_000_000)
            .map { i ->
                ProductDto(null, "product-$i", i)
            }
            //.delayElements(Duration.ofSeconds(2))

        productClient.uploadProducts(flux)
            .doOnNext { response -> logger.info("received: $response") }
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()

    }

    @Test
    fun download() {
        productClient.downloadProducts()
            .map(ProductDto::toString)
            .`as`{ flux -> FileWriter(Path.of("products.txt")).create(flux) }
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()
    }

}
