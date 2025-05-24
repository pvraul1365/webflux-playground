package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec07.dto.CalculatorResponse
import mu.KLogging
import org.junit.jupiter.api.Test
import org.springframework.http.ProblemDetail
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.doOnError
import reactor.test.StepVerifier

class Lec05ErrorResponseTest : AbstractWebClient() {

    companion object : KLogging()

    private val client: WebClient = super.createWebClient()

    @Test
    fun handlingError() {

        this.client.get()
            .uri("/{lec}/calculator/{a}/{b}", "lec05", 10, 20)
            .header("operation", "@")
            .retrieve()
            .bodyToMono(CalculatorResponse::class.java)
            .doOnError(WebClientResponseException::class) {
                val detail = it.getResponseBodyAs(ProblemDetail::class.java)
                logger.info("$detail")
            }
            .onErrorReturn(WebClientResponseException.InternalServerError::class.java, CalculatorResponse(0,0, null, 0.0))
            .onErrorReturn(WebClientResponseException.BadRequest::class.java, CalculatorResponse(0,0, null, -1.0))
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()

    }

    @Test
    fun exchange() {

        this.client.get()
            .uri("/{lec}/calculator/{a}/{b}", "lec05", 10, 20)
            .header("operation", "+")
            .exchangeToMono(this::decode)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()

    }

    private fun decode(clientResponse: ClientResponse): Mono<CalculatorResponse> {

        logger.info("Status code: ${clientResponse.statusCode()}")
        if (clientResponse.statusCode().isError) {
            return clientResponse.bodyToMono(ProblemDetail::class.java)
                .doOnNext { logger.info("$it") }
                .then(Mono.empty())
        }

        return clientResponse.bodyToMono(CalculatorResponse::class.java)
    }

}