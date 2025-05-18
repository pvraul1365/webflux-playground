package com.rperezv.webflux_playground.sec03.controller

import com.rperezv.webflux_playground.sec03.dto.CustomerDto
import com.rperezv.webflux_playground.sec03.service.CustomerService
import com.rperezv.webflux_playground.sec04.exceptions.ApplicationExceptions
import com.rperezv.webflux_playground.sec04.validator.RequestValidator
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("$CUSTOMER_PATH/paginated")
    fun allCustomers(@RequestParam(defaultValue = "1") page: Int, @RequestParam(defaultValue = "5") size: Int): Mono<Page<CustomerDto>> {
        return customerService.getAllCustomers(page, size)
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    fun deleteCustomer(@PathVariable customerId: String): Mono<Void> {

        return customerService.deleteCustomerById(customerId)
            .filter { it }
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .then()

    }

    @PutMapping(CUSTOMER_PATH_ID)
    fun updateCustomer(@PathVariable customerId: String, @RequestBody monoDto: Mono<CustomerDto>)
                : Mono<CustomerDto> {

        return monoDto.transform(RequestValidator.validate())
            .`as`{ validatedMonoDto -> customerService.updateCustomer(customerId, validatedMonoDto) }
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))

    }

    @PostMapping(CUSTOMER_PATH)
    fun saveCustomer(@RequestBody monoDto: Mono<CustomerDto>): Mono<ResponseEntity<Void>> {

        return monoDto.transform(RequestValidator.validate())
            .`as`(customerService::saveCustomer)
            .map { saveDto ->
                ResponseEntity.created(UriComponentsBuilder
                    .fromPath("/api/v1$CUSTOMER_PATH_ID")
                    .build(saveDto.id)
            ).build()
        }

    }

    @GetMapping(CUSTOMER_PATH)
    fun allCustomers(): Flux<CustomerDto> = customerService.getAllCustomers()

    @GetMapping(CUSTOMER_PATH_ID)
    fun getCustomer(@PathVariable customerId: String): Mono<CustomerDto> {

        return customerService.getCustomerById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
    }

}