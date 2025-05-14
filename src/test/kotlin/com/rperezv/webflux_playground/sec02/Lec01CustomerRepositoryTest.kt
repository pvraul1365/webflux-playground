package com.rperezv.webflux_playground.sec02

import com.rperezv.webflux_playground.sec02.domain.Customer
import com.rperezv.webflux_playground.sec02.repository.CustomerRepository
import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import reactor.test.StepVerifier

class Lec01CustomerRepositoryTest() : AbstractTest() {

    companion object : KLogging()

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun updateCustomer() {
        val updatedName = "ethan"
        customerRepository.findByName("noel")
            .doOnNext { c -> c.name = updatedName }
            .flatMap { c -> customerRepository.save(c) }
            .doOnNext { c -> logger.info("updatedCustomer: $c") }
            .let { StepVerifier.create(it) }
            .assertNext { c ->
                Assertions.assertEquals(updatedName, c.name)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun findAllTest() {
        val flux = customerRepository.findAll()

        flux.doOnNext { c -> logger.info("$c") }
            .let { StepVerifier.create(it) }
            .expectNextCount(10)
            .expectComplete()
            .verify()

    }

    @Test
    fun findByIdTest() {
        val mono = customerRepository.findById("6822238c85fa2b2f752adf79")

        mono.doOnNext { c -> logger.info("$c") }
            .let { StepVerifier.create(it) }
            .assertNext { c ->
                Assertions.assertEquals("jake", c.name)
                Assertions.assertEquals("jake@gmail.com", c.email)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun findByEmailTest() {
        val mono = customerRepository.findByEmail("mike@gmail.com")

        mono.doOnNext { c -> logger.info("$c") }
            .let { StepVerifier.create(it) }
            .assertNext { c ->
                Assertions.assertEquals("mike", c.name)
                Assertions.assertEquals("mike@gmail.com", c.email)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun findByNameTest() {
        val mono = customerRepository.findByName("mike")

        mono.doOnNext { c -> logger.info("$c") }
            .let { StepVerifier.create(it) }
            .assertNext { c ->
                Assertions.assertEquals("mike", c.name)
                Assertions.assertEquals("mike@gmail.com", c.email)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun findByEmailEndingWith() {
        val flux = customerRepository.findByEmailEndingWith("ke@gmail.com")

        flux.doOnNext { c -> logger.info("$c") }
            .let { StepVerifier.create(it) }
            .assertNext { c ->
                Assertions.assertEquals("mike", c.name)
                Assertions.assertEquals("mike@gmail.com", c.email)
            }
            .assertNext { c ->
                Assertions.assertEquals("jake", c.name)
                Assertions.assertEquals("jake@gmail.com", c.email)
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun insertAndDeleteCustomer() {
        val customer = Customer(null, "marshal", "marshall@gmail.com")

        customerRepository.save(customer)
            .doOnNext { c -> logger.info("savedCustomer: $c") }
            .flatMap { savedCustomer ->
                customerRepository.deleteById(savedCustomer.id!!)
                    .then(customerRepository.findById(savedCustomer.id!!))
            }
            .let { StepVerifier.create(it) }
            .expectNextCount(0)
            .expectComplete()
            .verify()
    }

}