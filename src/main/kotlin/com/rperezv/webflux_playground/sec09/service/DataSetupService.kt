package com.rperezv.webflux_playground.sec09.service

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import io.netty.util.internal.ThreadLocalRandom
import org.springframework.boot.CommandLineRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

class DataSetupService(
    private val productService: ProductService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        Flux.range(1, 1000)
            .delayElements(Duration.ofSeconds(1))
            .map { i -> ProductDto(null, "product-${i}", ThreadLocalRandom.current().nextInt(1, 100)) }
            .flatMap { dto -> productService.saveProduct(Mono.just(dto))}
            .subscribe()
    }

}