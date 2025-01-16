package com.prueba.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.lettuce.core.api.StatefulRedisConnection
import jakarta.inject.Singleton

@Singleton
open class RedisService(
    private val connection: StatefulRedisConnection<String, String>,
) : CacheServiceInterface {
    companion object {
        private const val REDIS_PREFIX = "marvel:"
    }

    private val objectMapper = ObjectMapper()

    init {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun <T : Any?> getKey(key: String, classRef: Class<T>): T? {
        return try {
            this.connection.sync().get(REDIS_PREFIX + key)?.let {
                this.objectMapper.readValue(it, classRef)
            }
        } catch (ex: Exception) {
            null
        }
    }

    override fun <T : Any?> setKey(key: String, ttl: Long, data: T) {
        try {
            this.connection.sync().setex(REDIS_PREFIX + key, ttl, this.objectMapper.writeValueAsString(data))
        } catch (ex: Exception) {

        }
    }
}