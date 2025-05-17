package com.rperezv.webflux_playground.sec03.service

import com.rperezv.webflux_playground.sec02.repository.CustomerRepository
import com.rperezv.webflux_playground.sec03.dto.CustomerDto
import com.rperezv.webflux_playground.sec03.mapper.EntityDtoMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CustomerService(val customerRepository: CustomerRepository) {

    fun getAllCustomers(page: Int, size: Int): Mono<Page<CustomerDto>> {
        var pageRequest = PageRequest.of(page -1, size)
        return customerRepository.findBy(pageRequest)
            .map(EntityDtoMapper::toDto)
            .collectList()
            .zipWith(customerRepository.count())
            .map { t -> PageImpl(t.t1, pageRequest, t.t2) }
    }

    fun getAllCustomers(): Flux<CustomerDto> = customerRepository.findAll().map(EntityDtoMapper::toDto)

    fun getCustomerById(id: String): Mono<CustomerDto>
        = customerRepository.findById(id).map(EntityDtoMapper::toDto)

    fun saveCustomer(customerDtoMono: Mono<CustomerDto>): Mono<CustomerDto> {
         return customerDtoMono.map(EntityDtoMapper::toEntity)
             .flatMap(customerRepository::save)
             .map(EntityDtoMapper::toDto)
    }

    fun updateCustomer(id: String, customerDtoMono: Mono<CustomerDto>): Mono<CustomerDto> {
        return customerRepository.findById(id)
            .flatMap { entity -> customerDtoMono }
            .map(EntityDtoMapper::toEntity)
            .doOnNext { customer -> customer.id = id }
            .flatMap(customerRepository::save)
            .map(EntityDtoMapper::toDto)
    }

    fun deleteCustomerById(id: String): Mono<Boolean> = customerRepository.deleteCustomerById(id)

}