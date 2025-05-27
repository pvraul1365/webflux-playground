package com.rperezv.webflux_playground.sec09.service

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import com.rperezv.webflux_playground.sec08.mapper.EntityDtoMapper
import com.rperezv.webflux_playground.sec08.repository.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks

@Service
class ProductService(
    private val repository: ProductRepository,
    private val sink: Sinks.Many<ProductDto>
) {

    fun saveProduct(mono: Mono<ProductDto>): Mono<ProductDto> {
        return mono.map(EntityDtoMapper::toEntity)
            .flatMap(repository::save)
            .map(EntityDtoMapper::toDto)
            .doOnNext { dto -> sink.tryEmitNext(dto) }
    }

    fun productStream(): Flux<ProductDto> {
        return sink.asFlux()
    }

    fun saveProducts(flux: Flux<ProductDto>): Flux<ProductDto> {
        return flux.map(EntityDtoMapper::toEntity)
            .`as`(repository::saveAll)
            .map(EntityDtoMapper::toDto)
    }

    fun getProductsCount(): Mono<Long> {
        return repository.count()
    }

    fun allProducts(): Flux<ProductDto> {
        return repository.findAll()
            .map(EntityDtoMapper::toDto)
    }
}