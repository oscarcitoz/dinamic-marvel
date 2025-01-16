package com.prueba.externals.marvel.responses

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
class OrchestratorResponse {
    val code: Int = 0
    val status: String = ""
    val data: DataOrchestrator? = null
}

@Serdeable
@Introspected
class DataOrchestrator {
    val offset: Int = 0
    val limit: Int = 0
    val total: Int = 0
    val count: Int = 0
    val results: List<Map<String?, Any?>> = listOf()
}