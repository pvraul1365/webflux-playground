package com.rperezv.webflux_playground.sec07

import com.rperezv.webflux_playground.sec07.dto.Product
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration


class Lec01MonoTest : AbstractWebClient() {

    private val client: WebClient = createWebClient()

    @Test
    fun simpleGet() {
        this.client.get()
            .uri("/lec01/product/1")
            .retrieve()
            .bodyToMono(Product::class.java)
            .log()
            //.doOnNext (println())
            .subscribe()

        Thread.sleep(Duration.ofSeconds(2))
    }

}