package com.prueba.services

import com.prueba.externals.marvel.responses.OrchestratorResponse
import com.prueba.requests.OrchestratorRequest
import reactor.core.publisher.Mono

interface OrchestratorServiceInterface {
    fun resolve(orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse>
    fun resolveRecursive(orchestratorRequests: List<OrchestratorRequest>): Mono<List<OrchestratorResponse>>
    fun resolveWithDependency(orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse>
}