package com.prueba.configurations.entities

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import java.io.InputStream

@Singleton
class EndpointConfiguration() {
    private val cachedConfigurations = mutableMapOf<String, Api>()

    fun resolveConfiguration(name: String): Api {
        cachedConfigurations[name]?.let {
            return it
        }

        val inputStream: InputStream =
            this.javaClass.classLoader.getResourceAsStream("configurations/$name.json") ?: throw HttpStatusException(
                HttpStatus.NOT_FOUND,
                "endpoint $name not configured"
            )

        val configuration = ObjectMapper().readValue(inputStream, object : TypeReference<Api>() {})
        cachedConfigurations[name] = configuration

        return configuration
    }
}