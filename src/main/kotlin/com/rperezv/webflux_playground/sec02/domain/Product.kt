package com.rperezv.webflux_playground.sec02.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
data class Product(

    @Id
    val id: String? = null,
    val description: String,
    val price: Int

)
