package com.rperezv.webflux_playground.sec05.filter

import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Order(2)
//@Service
class AuthorizationWebFilter : WebFilter {

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        val category = exchange.getAttribute<Category>("category") ?: Category.STANDARD
        return when (category) {
            Category.STANDARD -> standard(exchange, chain)
            Category.PRIME -> prime(exchange, chain)
        }
    }

    fun prime(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void?> {
        return chain.filter(exchange)
    }

    fun standard(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void?> {
        val isGet = HttpMethod.GET.equals(exchange.request.method)
        if (isGet) {
            return chain.filter(exchange)
        }

        return Mono.fromRunnable {
            exchange.response.statusCode = HttpStatus.FORBIDDEN
        }
    }

}