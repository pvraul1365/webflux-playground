package com.rperezv.webflux_playground.sec02.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("customers")
data class Customer(

    @Id
    var id: String? = null,
    var name: String,
    var email: String

)
