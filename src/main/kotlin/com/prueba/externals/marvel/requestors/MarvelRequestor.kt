package com.prueba.externals.marvel.requestors

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.prueba.externals.marvel.responses.OrchestratorResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.reactor.http.client.ReactorHttpClient
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Singleton
class MarvelRequestor(
    @Client("marvel") private val marvelClient: ReactorHttpClient,
) : MarvelRequestorInterface {
    private val objectMapper = ObjectMapper()

    init {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun make(path: String, params: Map<String, String>): Mono<OrchestratorResponse> {
        val request = HttpRequest.GET<Any>(path)
        params.forEach { (key, value) ->
            request.parameters.add(key, value)
        }

        return marvelClient.exchange(request, OrchestratorResponse::class.java).map {
            val body = it.getBody(LinkedHashMap::class.java).get()
            this.objectMapper.convertValue(body, OrchestratorResponse::class.java)
        }.onErrorMap {
            val error = (it as HttpClientResponseException).response.getBody(String::class.java).get()

            throw HttpStatusException(HttpStatus.BAD_REQUEST, "Server Client Error: $error")
        }
    }
}