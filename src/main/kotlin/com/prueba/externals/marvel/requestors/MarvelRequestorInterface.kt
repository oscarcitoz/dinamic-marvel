package com.prueba.externals.marvel.requestors

import com.prueba.externals.marvel.responses.OrchestratorResponse
import reactor.core.publisher.Mono

interface MarvelRequestorInterface {
    fun make(path: String, params: Map<String, String>): Mono<OrchestratorResponse>
}