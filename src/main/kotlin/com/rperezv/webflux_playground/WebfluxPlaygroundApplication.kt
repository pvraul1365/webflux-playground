package com.rperezv.webflux_playground

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.rperezv.webflux_playground.sec02"])
class WebfluxPlaygroundApplication

fun main(args: Array<String>) {
	runApplication<WebfluxPlaygroundApplication>(*args)
}
