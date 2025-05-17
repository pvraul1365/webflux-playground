package com.rperezv.webflux_playground.sec04.exceptions

class CustomerNotFoundException(id: String) : RuntimeException("Customer [id=$id] is not found")