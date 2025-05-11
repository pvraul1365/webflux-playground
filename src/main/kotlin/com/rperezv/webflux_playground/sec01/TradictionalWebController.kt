package com.rperezv.webflux_playground.sec01

import mu.KLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
@RequestMapping("traditional")
class TradictionalWebController {

    companion object : KLogging()

    val restClient: RestClient = RestClient.builder()
        .requestFactory(JdkClientHttpRequestFactory())
        .baseUrl("http://localhost:7070")
        .build()

    @GetMapping("products")
    fun getProducts(): List<Product>? {
        val list = restClient.get()
            .uri("/demo01/products/notorious")
            .retrieve()
            .body(object: ParameterizedTypeReference<List<Product>>() {})

        logger.info("received response: $list")

        return list
    }



}