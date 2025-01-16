package com.prueba.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.prueba.containers.OrchestratorContainer
import com.prueba.extensions.getValueByPath
import com.prueba.externals.marvel.requestors.MarvelRequestorInterface
import com.prueba.externals.marvel.responses.OrchestratorResponse
import com.prueba.managers.FileManager
import com.prueba.managers.ManagerInterface
import com.prueba.requests.OrchestratorRequest
import com.prueba.requests.Parameter
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Singleton
class OrchestratorService(
    private val managers: List<ManagerInterface>,
    private val marvelRequestorInterface: MarvelRequestorInterface,
    private val cacheServiceInterface: CacheServiceInterface,
    private val fileManager: FileManager

) : OrchestratorServiceInterface {
    companion object {
        private const val KEY = "ORCHESTRATOR_"
        private const val ONE_HOUR_CACHE = 3600L
    }

    override fun resolve(orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse> {
        val container = OrchestratorContainer().apply {
            this.orchestratorRequest = orchestratorRequest
        }

        this.managers.forEach { manager ->
            manager.apply(container)
        }

        return this.getResponse(container, orchestratorRequest).map { response ->
            response.data?.results?.let {
                container.orchestratorRequest?.orderByKey?.let { orderByKey ->
                    if (container.orchestratorRequest?.orderDescend == true) {
                        response.data.results =
                            response.data.results.sortedByDescending { it.getValueByPath(orderByKey).toInt() }
                    } else response.data.results =
                        response.data.results.sortedBy { it.getValueByPath(orderByKey).toInt() }
                }
            }

            response
        }
    }

    private fun getResponse(
        container: OrchestratorContainer,
        orchestratorRequest: OrchestratorRequest
    ): Mono<OrchestratorResponse> {
        val cacheKey = orchestratorRequest.getKeyForCache(KEY)
        val cacheValue =
            this.cacheServiceInterface.getKey(cacheKey, OrchestratorResponse::class.java)

        return if (cacheValue != null) {
            Mono.just(cacheValue)
        } else {
            this.marvelRequestorInterface.make(container.wrapperForRequest!!.path, container.wrapperForRequest!!.params)
                .flatMap { response ->
                    response.name = orchestratorRequest.name

                    Mono.fromCallable {
                        this.cacheServiceInterface.setKey(cacheKey, ONE_HOUR_CACHE, response)
                    }
                        .subscribeOn(Schedulers.boundedElastic())
                        .thenReturn(response)
                }
        }
    }

    override fun resolveRecursive(orchestratorRequests: List<OrchestratorRequest>): Mono<List<OrchestratorResponse>> {
        return Flux.fromIterable(orchestratorRequests)
            .flatMap({ request ->
                resolve(request)
            }, 5).subscribeOn(Schedulers.boundedElastic()).collectList()
    }

    override fun resolveWithDependency(orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse> {
        val container = OrchestratorContainer().apply {
            this.orchestratorRequest = orchestratorRequest
        }

        this.fileManager.apply(container)

        if (container.dependsApi == null) throw HttpStatusException(
            HttpStatus.BAD_REQUEST,
            "request without dependencies"
        )

        val orchestratorRequestRecursive = container.dependsApi!!.map {
            val newOrchestratorRequest = orchestratorRequest.copy()
            newOrchestratorRequest.name = it.name
            newOrchestratorRequest.orderByKey = null
            newOrchestratorRequest
        }

        val objectMapper = ObjectMapper()

        return this.resolveRecursive(orchestratorRequestRecursive).flatMap { orchestratorResponses ->
            container.api!!.configuration.parameters.forEach { parameterConfiguration ->
                parameterConfiguration.from?.let { from ->
                    val fromSplit = from.split("->")
                    val dataForInject = orchestratorResponses.first { it.name == fromSplit[0] }
                    val json = objectMapper.writeValueAsString(dataForInject)

                    try {
                        val valueForInject = JsonPath.read<Any>(json, fromSplit[1])
                        orchestratorRequest.parameters =
                            listOf(Parameter(parameterConfiguration.key, valueForInject.toString()))
                    } catch (ex: Exception) {
                        throw HttpStatusException(HttpStatus.BAD_REQUEST, "Empty Dependencies")
                    }
                }
            }

            this.resolve(orchestratorRequest)
        }
    }
}