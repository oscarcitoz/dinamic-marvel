package com.prueba.services

import com.prueba.configurations.entities.EndpointConfiguration
import com.prueba.containers.OrchestratorContainer
import com.prueba.extensions.generateCacheKey
import com.prueba.externals.marvel.requestors.MarvelRequestorInterface
import com.prueba.externals.marvel.responses.OrchestratorResponse
import com.prueba.managers.ManagerInterface
import com.prueba.requests.OrchestratorRequest
import jakarta.inject.Singleton
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Singleton
class OrchestratorService(
    private val endpointConfiguration: EndpointConfiguration,
    private val managers: List<ManagerInterface>,
    private val marvelRequestorInterface: MarvelRequestorInterface,
    private val cacheServiceInterface: CacheServiceInterface

) : OrchestratorServiceInterface {
    companion object {
        private const val KEY = "ORCHESTRATOR_"
        private const val ONE_HOUR_CACHE = 3600L
    }

    override fun resolve(orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse> {
        val keyCache = KEY + orchestratorRequest.name + "_" + orchestratorRequest.parametersToMap().generateCacheKey()
        val cacheValue = this.cacheServiceInterface.getKey(keyCache, OrchestratorResponse::class.java)

        return if (cacheValue != null) {
            Mono.just(cacheValue)
        } else {
            val container = OrchestratorContainer().apply {
                this.orchestratorRequest = orchestratorRequest
                this.api = endpointConfiguration.resolveConfiguration(orchestratorRequest.name)
            }

            this.managers.forEach { manager ->
                manager.apply(container)
            }

            this.marvelRequestorInterface.make(container.wrapperForRequest!!.path, container.wrapperForRequest!!.params)
                .flatMap { response ->
                    Mono.fromCallable {
                        this.cacheServiceInterface.setKey(keyCache, ONE_HOUR_CACHE, response)
                    }
                        .subscribeOn(Schedulers.boundedElastic())
                        .thenReturn(response)
                }
        }
    }
}