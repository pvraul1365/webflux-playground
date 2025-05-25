package com.rperezv.webflux_playground.sec08.dto

import java.util.UUID

data class UploadResponse(
    val confirmationId: UUID,
    val productsCount: Long
)
