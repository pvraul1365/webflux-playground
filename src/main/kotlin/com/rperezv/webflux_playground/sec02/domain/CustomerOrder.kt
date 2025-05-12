package com.rperezv.webflux_playground.sec02.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("customer_orders")
data class CustomerOrder(

    @Id
    val orderId: String? = null,
    val customerId: String,
    val productId: String,
    val amount: Int,
    val orderDate: Instant = Instant.now()

)
