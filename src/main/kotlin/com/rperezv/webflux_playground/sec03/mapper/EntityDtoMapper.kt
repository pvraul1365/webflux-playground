package com.rperezv.webflux_playground.sec03.mapper

import com.rperezv.webflux_playground.sec02.domain.Customer
import com.rperezv.webflux_playground.sec03.dto.CustomerDto

object EntityDtoMapper {

    fun toEntity(dto: CustomerDto): Customer {
        return Customer(
            id = dto.id,
            name = dto.name,
            email = dto.email
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