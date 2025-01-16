package com.prueba.services

interface CacheServiceInterface {
    fun <T : Any?> getKey(key: String, classRef: Class<T>): T?
    fun <T : Any?> setKey(key: String, ttl: Long, data: T)
}