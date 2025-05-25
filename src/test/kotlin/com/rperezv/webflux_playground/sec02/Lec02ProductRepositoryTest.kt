package com.rperezv.webflux_playground.sec02

import com.rperezv.webflux_playground.sec08.repository.ProductRepository
import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    @Test
    fun findAllBy() {
        val pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "price"))

        productRepository.findAllBy(pageRequest)
            .doOnNext { p -> logger.info("retrievedProduct: $p") }
            .let { StepVerifier.create(it) }
            .assertNext { p -> Assertions.assertEquals(200, p.price) }
            .assertNext { p -> Assertions.assertEquals(250, p.price) }
            .assertNext { p -> Assertions.assertEquals(300, p.price) }
            .expectComplete()
            .verify()

    }

}