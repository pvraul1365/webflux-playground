package com.rperezv.webflux_playground.sec04.advice

import com.rperezv.webflux_playground.sec04.exceptions.CustomerNotFoundException
import com.rperezv.webflux_playground.sec04.exceptions.InvalidInputException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.URI

@ControllerAdvice
class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleException(ex: CustomerNotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.message ?: "Customer not found")

        problemDetail.type = URI.create("http://example.com/problems/customer-not-found")
        problemDetail.title = "Customer Not Found"

        return problemDetail
    }

    @ExceptionHandler(InvalidInputException::class)
    fun handleException(ex: InvalidInputException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
            ex.message ?: "Invalid input")

        problemDetail.type = URI.create("http://example.com/problems/invalid-input")
        problemDetail.title = "Invalid Input"

        return problemDetail
    }

}