package com.rperezv.webflux_playground.sec08.service

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import com.rperezv.webflux_playground.sec08.mapper.EntityDtoMapper
import com.rperezv.webflux_playground.sec08.repository.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProductService(
    private val repository: ProductRepository
) {

    fun saveProducts(flux: Flux<ProductDto>): Flux<ProductDto> {
        return flux.map(EntityDtoMapper::toEntity)
            .`as`(repository::saveAll)
            .map(EntityDtoMapper::toDto)
    }

    fun getProductsCount(): Mono<Long> {
        return repository.count()
    }

}