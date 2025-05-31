package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec10.dto.Product
import com.rperezv.webflux_playground.sec07.AbstractWebClient
import mu.KLogging
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.util.*


class Lec09ExchangeFilterTest : AbstractWebClient() {

    companion object : KLogging()

    private val client: WebClient = super.createWebClient {
            builder -> builder.filter(tokenGenerator())
        .filter(requestLogger())
    }

    @Test
    fun exchangeFilter() {
        this.client.get()
            .uri("/{lec}/product/{id}", "lec09", 1)
            .attribute("enable-logging", true )
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()
    }

    private fun tokenGenerator(): ExchangeFilterFunction {
        return ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
            val token = UUID.randomUUID().toString().replace("-", "")
            logger.info("generated token: {}", token)
            val modifiedRequest = ClientRequest.from(request)
                    .headers { headers -> headers.setBearerAuth(token) }
                    .build()
            next.exchange(modifiedRequest)
        }
    }

    private fun requestLogger(): ExchangeFilterFunction {
        return ExchangeFilterFunction { request: ClientRequest, next: ExchangeFunction ->
            val isEnabled = request.attributes().getOrDefault("enable-logging", false) as Boolean
            if (isEnabled) {
                logger.info("request url - {}: {}", request.method(), request.url())
            }
            next.exchange(request)
        }
    }
}