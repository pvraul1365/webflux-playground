package com.rperezv.webflux_playground.sec05.filter

import mu.KLogging
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Order(1)
//@Service
class AuthenticationWebFilter : WebFilter {

    companion object : KLogging() {
        private val TOKEN_CATEGORY_MAP: Map<String, Category> = mapOf(
            "secret123" to Category.STANDARD,
            "secret456" to Category.PRIME
        )
    }

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {

        val token = exchange.request.headers.getFirst("auth-token")
        if (token != null && TOKEN_CATEGORY_MAP.containsKey(token)) {
            exchange.attributes["category"] = TOKEN_CATEGORY_MAP[token]

            return chain.filter(exchange)
        }

        return Mono.fromRunnable {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        }

    }



    

}