package com.rperezv.webflux_playground.sec02

import com.rperezv.webflux_playground.sec02.repository.ProductRepository
import mu.KLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class Lec02ProductRepositoryTest : AbstractTest() {

    companion object : KLogging()

    @Autowired
    lateinit var productRepository: ProductRepository

    @Test
    fun findByPriceRange() {

        productRepository.findByPriceBetween(749, 1001)
            .doOnNext { p -> logger.info("retrievedProduct: $p") }
            .let { StepVerifier.create(it) }
            .expectNextCount(3)
            .expectComplete()
            .verify()

    }

}