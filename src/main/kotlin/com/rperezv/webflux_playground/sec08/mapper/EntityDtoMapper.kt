package com.rperezv.webflux_playground.sec08.mapper

import com.rperezv.webflux_playground.sec02.domain.Customer
import com.rperezv.webflux_playground.sec03.dto.CustomerDto
import com.rperezv.webflux_playground.sec08.dto.ProductDto
import com.rperezv.webflux_playground.sec08.entity.Product

object EntityDtoMapper {

    fun toEntity(dto: ProductDto): Product {
        return Product(
            id = dto.id,
            description = dto.description,
            price = dto.price
        )
    }

    fun toDto(entity: Product): ProductDto {
        return ProductDto(
            id = entity.id,
            description = entity.description,
            price = entity.price
        )
    }

    fun toEntity(dto: CustomerDto): Customer {
        return Customer(
            id = dto.id,
            name = dto.name!!,
            email = dto.email!!
        )
    }

    fun toDto(entity: Customer): CustomerDto {
        return CustomerDto(
            id = entity.id,
            name = entity.name,
            email = entity.email
        )
    }

}