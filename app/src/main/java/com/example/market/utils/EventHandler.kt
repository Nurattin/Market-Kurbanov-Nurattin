package com.example.market.utils

interface EventHandler<T> {
    fun obtainEvent(event: T)
}