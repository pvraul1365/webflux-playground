package com.rperezv.webflux_playground.sec08.dto

data class ProductDto(
    val id: String? = null,
    var description: String,
    var price: Int
)