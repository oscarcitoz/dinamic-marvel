package com.prueba.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.prueba.extensions.generateCacheKey
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
data class OrchestratorRequest(
    var name: String,
    @field:JsonProperty("parameters")
    var parameters: List<Parameter>,

    @field:JsonProperty("order_by_key")
    var orderByKey: String? = null,

    @field:JsonProperty("order_descend")
    var orderDescend: Boolean? = null,
) {
    fun parametersToMap(): Map<String, String> {
        return this.parameters.associate { it.key to it.value }
    }

    fun getKeyForCache(defaultKey: String): String {
        return defaultKey + this.name + "_" + this.parametersToMap().generateCacheKey()
    }
}

@Serdeable
@Introspected
data class Parameter(val key: String, val value: String)