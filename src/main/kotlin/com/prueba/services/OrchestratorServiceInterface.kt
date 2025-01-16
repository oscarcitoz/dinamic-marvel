package com.prueba.services

import com.prueba.externals.marvel.responses.BaseResponse
import com.prueba.externals.marvel.responses.ComicResponse
import com.prueba.externals.marvel.responses.OrchestratorResponse
import com.prueba.requests.OrchestratorRequest
import reactor.core.publisher.Mono

interface OrchestratorServiceInterface {
    fun resolve(orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse>
}