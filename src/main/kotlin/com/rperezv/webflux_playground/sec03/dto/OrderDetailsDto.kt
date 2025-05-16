package com.rperezv.webflux_playground.sec03.dto

import java.time.Instant

data class OrderDetailsDto (

    val orderId: String,
    var customerName: String,
    var productName: String,
    var amount: Int,
    var orderDate: Instant

)
