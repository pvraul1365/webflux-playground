package com.rperezv.webflux_playground.sec10.dto

data class CalculatorResponse(
    val first: Int,
    val second: Int,
    val operation: String?,
    val result: Double
)
