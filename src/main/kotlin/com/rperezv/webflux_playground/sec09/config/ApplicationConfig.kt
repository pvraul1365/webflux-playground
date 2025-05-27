package com.rperezv.webflux_playground.sec09.config

import com.rperezv.webflux_playground.sec08.dto.ProductDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Sinks

@Configuration
class ApplicationConfig {

    @Bean
    fun sink(): Sinks.Many<ProductDto> {
        return Sinks.many().replay().limit(1)
    }

}