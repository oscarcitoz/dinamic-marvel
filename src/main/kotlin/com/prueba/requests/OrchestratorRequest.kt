package com.prueba.requests

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
data class OrchestratorRequest(
    val name: String,
    @field:JsonProperty("parameters")
    val parameters: List<Parameter>,
) {
    fun parametersToMap(): Map<String, String> {
        return this.parameters.associate { it.key to it.value }
    }
}

@Serdeable
@Introspected
data class Parameter(val key: String, val value: String)