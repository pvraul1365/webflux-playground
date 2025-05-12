package com.rperezv.webflux_playground.sec02.repository

import com.rperezv.webflux_playground.sec02.domain.CustomerOrder
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerOrderRepository : ReactiveMongoRepository<CustomerOrder, String> {
}