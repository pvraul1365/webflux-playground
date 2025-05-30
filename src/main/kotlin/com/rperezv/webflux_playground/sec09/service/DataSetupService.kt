package com.rperezv.webflux_playground.sec09.service

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import io.netty.util.internal.ThreadLocalRandom
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class DataSetupService(
    private val productService: ProductService
) : CommandLineRunner {

    companion object : KLogging()

    override fun run(vararg args: String?) {
        Flux.range(1, 1000)
            .delayElements(Duration.ofSeconds(1))
            .map { i ->
                val dto = ProductDto(null, "product-$i", ThreadLocalRandom.current().nextInt(1, 100))
                logger.info("dto generated: $dto")
                dto
            }
            .flatMap { dto -> productService.saveProduct(Mono.just(dto))}
            .subscribe()
    }

}