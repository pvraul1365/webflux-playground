package com.rperezv.webflux_playground.sec07

import mu.KLogging
import org.springframework.web.reactive.function.client.WebClient
import java.util.function.Consumer

abstract class AbstractWebClient {

    companion object : KLogging()

    protected fun <T> println(): Consumer<T> {
        return Consumer { item -> logger.info("received: $item") }
    }

    protected fun createWebClient(): WebClient {
        /*
            This method simply calls the second method, passing an empty lambda function as an argument ({ b -> {}}),
            which does not modify the builder (It uses the default configuration of the WebClient.Builder)
         */
        return createWebClient { _ ->  }
    }

    protected fun createWebClient(consumer: Consumer<WebClient.Builder>): WebClient {
        // It creates an instance of the WebClient.Builder. It sets a base URL. It applies the Consumer we pass in, allowing us to modify the builder. Finally, it builds the WebClient
        val builder = WebClient.builder()
            .baseUrl("http://localhost:7070/demo02")

        consumer.accept(builder)

        return builder.build()
    }

}