package com.rperezv.webflux_playground.sec08

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class FileWriter(private val path: Path) {

    private var writer: BufferedWriter? = null

    private fun createFile() {
        try {
            writer = Files.newBufferedWriter(path)
        } catch (e: IOException) {
            throw RuntimeException("Error creando archivo", e)
        }
    }

    private fun closeFile() {
        try {
            writer?.close()
        } catch (e: IOException) {
            throw RuntimeException("Error cerrando archivo", e)
        }
    }

    private fun write(content: String) {
        try {
            writer?.apply {
                write(content)
                newLine()
                flush()
            }
        } catch (e: IOException) {
            throw RuntimeException("Error escribiendo al archivo", e)
        }
    }

    fun create(flux: Flux<String>): Mono<Void> {
        return flux
            .doFirst { createFile() }
            .doOnNext { write(it) }
            .doFinally { closeFile() }
            .then()
    }

    companion object {
        fun writeToFile(flux: Flux<String>, path: Path): Mono<Void> {
            return FileWriter(path).create(flux)
        }
    }
}