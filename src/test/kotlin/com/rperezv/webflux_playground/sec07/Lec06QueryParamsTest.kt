package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec10.dto.CalculatorResponse
import com.rperezv.webflux_playground.sec07.AbstractWebClient
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.util.Map

class Lec06QueryParamsTest : AbstractWebClient() {

    private val client: WebClient = super.createWebClient()

    @Test
    fun uriBuilderVariables() {
        val path = "/lec06/calculator"
        val query = "first={first}&second={second}&operation={operation}"
        this.client.get()
            .uri { builder -> builder.path(path).query(query).build(10, 20, "+") }
            .retrieve()
            .bodyToMono(CalculatorResponse::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()
    }

    @Test
    fun uriBuilderMap() {
        val path = "/lec06/calculator"
        val query = "first={first}&second={second}&operation={operation}"
        val map = Map.of(
            "first", 10,
            "second", 20,
            "operation", "*"
        )
        this.client.get()
            .uri { builder -> builder.path(path).query(query).build(map) }
            .retrieve()
            .bodyToMono(CalculatorResponse::class.java)
            .log()
            .then()
            .`as`(StepVerifier::create)
            .expectComplete()
            .verify()
    }

}