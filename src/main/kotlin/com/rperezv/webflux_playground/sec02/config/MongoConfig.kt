package com.rperezv.webflux_playground.sec02.config

import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration

@Configuration
class MongoConfig: AbstractReactiveMongoConfiguration() {

    override fun getDatabaseName(): String = "userdb"

    @Bean
    override fun reactiveMongoClient() = MongoClients.create("mongodb://localhost:27017")

}