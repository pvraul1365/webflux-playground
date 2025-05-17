package com.rperezv.webflux_playground.sec03.repository

import com.rperezv.webflux_playground.sec02.domain.Customer
import mu.KLogging
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Mono

class CustomerRepositoryCustomImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
): CustomerRepositoryCustom {

    companion object : KLogging()

    override fun deleteCustomerById(id: String): Mono<Boolean> {
        val query = Query(Criteria.where("_id").`is`(id))

        return reactiveMongoTemplate.remove(query, Customer::class.java)
            .map { result ->
                logger.info("🧾 DeletedCount for id=$id: ${result.deletedCount}")
                result.deletedCount > 0
            }

    }

}