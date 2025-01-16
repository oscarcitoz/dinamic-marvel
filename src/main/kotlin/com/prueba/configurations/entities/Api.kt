package com.prueba.configurations.entities

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
class Api {
    var name: String = ""
    val configuration: Configuration = Configuration()
    @field:JsonProperty("depends_on")
    val dependsOn: List<String>? = null
}

@Serdeable
@Introspected
class Configuration {
    var url: String = ""

    @field:JsonProperty("parameters")
    val parameters: List<Parameter> = listOf()
}

@Serdeable
@Introspected
class Parameter {
    val key: String = ""
    val type: String = ""

    @field:JsonProperty("possible_values")
    val possibleValues: List<String>? = null
    val required: Boolean? = null
    val from: String? = null
}