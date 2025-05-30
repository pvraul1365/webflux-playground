package com.rperezv.webflux_playground.sec09

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@AutoConfigureWebTestClient
@SpringBootTest
class ServerSentEventsTest {

    companion object : KLogging()

    @Autowired
    private lateinit var client: WebTestClient

    @Test
    fun serverSentEvents() {
        client.get()
            .uri("/api/v3/products/stream/80")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().is2xxSuccessful
            .returnResult(ProductDto::class.java)
            .getResponseBody()
            .take(3)
            .doOnNext { dto -> logger.info("received: $dto") }
            .collectList()
            .`as`(StepVerifier::create)
            .assertNext { list ->
                Assertions.assertEquals(3, list.size)
                Assertions.assertTrue(
                    list.stream().allMatch { p -> p.price <= 80}
                )
            }
            .expectComplete()
            .verify()

    }
}