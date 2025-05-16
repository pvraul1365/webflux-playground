package com.rperezv.webflux_playground.sec02.bootstrap

import com.rperezv.webflux_playground.sec02.domain.Customer
import com.rperezv.webflux_playground.sec02.domain.CustomerOrder
import com.rperezv.webflux_playground.sec02.domain.Product
import com.rperezv.webflux_playground.sec02.repository.CustomerOrderRepository
import com.rperezv.webflux_playground.sec02.repository.CustomerRepository
import com.rperezv.webflux_playground.sec02.repository.ProductRepository
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import reactor.core.publisher.Mono

//@Component
class DataInitializer(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
    private val customerOrderRepository: CustomerOrderRepository
) : CommandLineRunner {

    companion object : KLogging()

    override fun run(vararg args: String?) {
        val customers = listOf(
            Customer(name = "sam", email = "sam@gmail.com"),
            Customer(name = "mike", email = "mike@gmail.com"),
            Customer(name = "jake", email = "jake@gmail.com"),
            Customer(name = "emily", email = "emily@example.com"),
            Customer(name = "sophia", email = "sophia@example.com"),
            Customer(name = "liam", email = "liam@example.com"),
            Customer(name = "olivia", email = "olivia@example.com"),
            Customer(name = "noah", email = "noah@example.com"),
            Customer(name = "ava", email = "ava@example.com"),
            Customer(name = "ethan", email = "ethan@example.com")
        )

        val products = listOf(
            Product(description = "iphone 20", price = 1000),
            Product(description = "iphone 18", price = 750),
            Product(description = "ipad", price = 800),
            Product(description = "mac pro", price = 3000),
            Product(description = "apple watch", price = 400),
            Product(description = "macbook air", price = 1200),
            Product(description = "airpods pro", price = 250),
            Product(description = "imac", price = 2000),
            Product(description = "apple tv", price = 200),
            Product(description = "homepod", price = 300)
        )

        val resetAndInsert = customerOrderRepository.deleteAll()
            .thenMany(customerRepository.deleteAll())
            .thenMany(customerRepository.saveAll(customers))
            .thenMany(productRepository.deleteAll())
            .thenMany(productRepository.saveAll(products))
            .then(
                Mono.zip(
                    customerRepository.findByEmail("sam@gmail.com"),
                    customerRepository.findByEmail("mike@gmail.com"),
                    customerRepository.findByEmail("jake@gmail.com"),
                    productRepository.findByDescription("iphone 20"),
                    productRepository.findByDescription("iphone 18"),
                    productRepository.findByDescription("mac pro"),
                    productRepository.findByDescription("ipad")
                )
            )
            .flatMapMany { tuple ->
                val sam = tuple.t1
                val mike = tuple.t2
                val jake = tuple.t3
                val iphone20 = tuple.t4
                val iphone18 = tuple.t5
                val macpro = tuple.t6
                val ipad = tuple.t7

                val orders = listOf(
                    // Sam buys an iphone 20 & iphone 18
                    CustomerOrder(customerId = sam.id!!,  productId = iphone20.id!!, amount = 950),
                    CustomerOrder(customerId = sam.id,  productId = iphone18.id!!, amount = 850),
                    // Mike buys iphone 20 & mac pro
                    CustomerOrder(customerId = mike.id!!, productId = iphone20.id, amount = 975),
                    CustomerOrder(customerId = mike.id, productId = macpro.id!!, amount = 2999),
                    // Jake buys iphone 18 & ipad
                    CustomerOrder(customerId = jake.id!!, productId = iphone18.id, amount = 750),
                    CustomerOrder(customerId = jake.id, productId = ipad.id!!, amount = 775)
                )

                customerOrderRepository.saveAll(orders)
            }

            resetAndInsert
                .doOnNext { logger.info("✅ Orden inserted: $it") }
                .doOnComplete { logger.info("🚀 Initialization completed.") }
                .doOnError { logger.error("❌ Error initializing data: ${it.message}") }
                .subscribe()
    }

}