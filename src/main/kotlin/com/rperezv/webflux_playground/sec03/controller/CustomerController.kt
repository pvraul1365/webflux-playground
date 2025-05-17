package com.rperezv.webflux_playground.sec03.controller

import com.rperezv.webflux_playground.sec03.dto.CustomerDto
import com.rperezv.webflux_playground.sec03.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1")
class CustomerController(
    val customerService: CustomerService
) {

    companion object {
        const val CUSTOMER_PATH = "/customers"
        const val CUSTOMER_PATH_ID = "$CUSTOMER_PATH/{customerId}"
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    fun deleteCustomer(@PathVariable customerId: String): Mono<Void>
            = customerService.deleteCustomerById(customerId)

    @PutMapping(CUSTOMER_PATH_ID)
    fun updateCustomer(@PathVariable customerId: String, @RequestBody monoDto: Mono<CustomerDto>): Mono<CustomerDto>
            = customerService.updateCustomer(customerId, monoDto)

    @PostMapping(CUSTOMER_PATH)
    fun saveCustomer(@RequestBody monoDto: Mono<CustomerDto>): Mono<ResponseEntity<Void>> {

        return customerService.saveCustomer(monoDto).map { saveDto ->
            ResponseEntity.created(UriComponentsBuilder
                .fromPath("/api/v1$CUSTOMER_PATH_ID")
                .build(saveDto.id)
            ).build()
        }

    }

    @GetMapping(CUSTOMER_PATH)
    fun allCustomers(): Flux<CustomerDto> = customerService.getAllCustomers()

    @GetMapping(CUSTOMER_PATH_ID)
    fun getCustomer(@PathVariable customerId: String): Mono<CustomerDto>
            = customerService.getCustomerById(customerId)

}