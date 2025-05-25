package com.rperezv.webflux_playground.sec08.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("products")
data class Product(

    @Id
    val id: String? = null,
    var description: String,
    var price: Int

)