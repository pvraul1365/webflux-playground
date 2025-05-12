package com.rperezv.webflux_playground.sec02.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("customers")
data class Customer(

    @Id
    val id: String? = null,
    val name: String,
    val email: String

)
