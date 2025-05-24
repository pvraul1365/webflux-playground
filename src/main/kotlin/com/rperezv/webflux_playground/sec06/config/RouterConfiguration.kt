package com.rperezv.webflux_playground.sec06.config

import com.rperezv.webflux_playground.sec04.exceptions.CustomerNotFoundException
import com.rperezv.webflux_playground.sec04.exceptions.InvalidInputException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RouterConfiguration(
    private val customerRequestHandler: CustomerRequestHandler,
    private val exceptionHandlerRouter: ApplicationExceptionHandlerRouter
) {

    @Bean
    fun customerRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v2/customers",  customerRequestHandler::allCustomer)
            .GET("/api/v2/customers/{customerId}",  customerRequestHandler::getCustomer)
            .POST("/api/v2/customers",  customerRequestHandler::saveCustomer)
            .PUT("/api/v2/customers/{customerId}",  customerRequestHandler::updateCustomer)
            .DELETE("/api/v2/customers/{customerId}",  customerRequestHandler::deleteCustomer)
            .onError(CustomerNotFoundException::class.java, exceptionHandlerRouter::handleException)
            .onError(InvalidInputException::class.java, exceptionHandlerRouter::handleException)
            .build()
    }

}