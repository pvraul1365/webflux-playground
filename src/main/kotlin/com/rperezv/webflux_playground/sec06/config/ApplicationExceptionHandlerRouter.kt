package com.rperezv.webflux_playground.sec06.config

import com.rperezv.webflux_playground.sec04.exceptions.CustomerNotFoundException
import com.rperezv.webflux_playground.sec04.exceptions.InvalidInputException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI
import java.util.function.Consumer

@Service
class ApplicationExceptionHandlerRouter {

   fun handleException(
       ex: CustomerNotFoundException,
       request: ServerRequest
   ): Mono<ServerResponse> {
       return handleException(
           HttpStatus.NOT_FOUND,
           ex,
           request
       ) { problem ->
           problem.type = URI.create("http://example.com/problems/customer-not-found")
           problem.title = "Customer Not Found"
       }
    }

   fun handleException(
       ex: InvalidInputException,
       request: ServerRequest
   ): Mono<ServerResponse> {
       return handleException(
           HttpStatus.BAD_REQUEST,
           ex,
           request
       ) { problem ->
           problem.type = URI.create("http://example.com/problems/invalid-input")
           problem.title = "Invalid Input"
       }
    }

    private fun handleException(status: HttpStatus, ex: Exception, request: ServerRequest, consumer: Consumer<ProblemDetail>)
            : Mono<ServerResponse> {

        val problemDetail = ProblemDetail.forStatusAndDetail(status,ex.message ?: "Invalid input")
        consumer.accept(problemDetail)
        problemDetail.instance = URI.create(request.path())

        return ServerResponse.status(status).bodyValue(problemDetail)
    }

}