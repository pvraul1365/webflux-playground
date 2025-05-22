package com.rperezv.webflux_playground.sec06.config

import com.rperezv.webflux_playground.sec03.dto.CustomerDto
import com.rperezv.webflux_playground.sec03.service.CustomerService
import com.rperezv.webflux_playground.sec04.exceptions.ApplicationExceptions
import com.rperezv.webflux_playground.sec04.validator.RequestValidator
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Service
class CustomerRequestHandler(private val customerService: CustomerService) {

    fun allCustomer(request: ServerRequest): Mono<ServerResponse> {

        return ServerResponse.ok().body(customerService.getAllCustomers(), CustomerDto::class.java)

        /*
        return customerService.getAllCustomers().`as` {
            flux -> ServerResponse.ok().body(flux, CustomerDto::class.java)
        }
        */
    }

    fun paginatedCustomer(request: ServerRequest): Mono<ServerResponse> {
        val page = request.queryParam("page").map(Integer::parseInt).orElse(1)
        val size = request.queryParam("size").map(Integer::parseInt).orElse(3)

        return customerService.getAllCustomers(page, size)
            .flatMap(ServerResponse.ok()::bodyValue)
    }

    fun getCustomer(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("customerId")

        /*
        return ServerResponse.ok().body(customerService.getCustomerById(id)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(id)),
            CustomerDto::class.java)
         */
        return customerService.getCustomerById(id)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
            .flatMap(ServerResponse.ok()::bodyValue)
    }

    fun saveCustomer(request: ServerRequest): Mono<ServerResponse> {

        return request.bodyToMono(CustomerDto::class.java)
            .transform(RequestValidator.validate())
            .`as` { validatedMonoDto -> customerService.saveCustomer(validatedMonoDto) }
            .flatMap(ServerResponse.ok()::bodyValue)
    }

    fun updateCustomer(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("customerId")

        return request.bodyToMono(CustomerDto::class.java)
            .transform(RequestValidator.validate())
            .`as` { validatedMonoDto -> customerService.updateCustomer(id, validatedMonoDto) }
            .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
            .flatMap(ServerResponse.ok()::bodyValue)
    }

    fun deleteCustomer(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("customerId")

        return customerService.deleteCustomerById(id)
            .filter { it }
            .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
            .then(ServerResponse.ok().build())
    }
}