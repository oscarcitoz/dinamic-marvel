package com.prueba.controllers

import com.prueba.externals.marvel.responses.BaseResponse
import com.prueba.externals.marvel.responses.ComicResponse
import com.prueba.externals.marvel.responses.OrchestratorResponse
import com.prueba.requests.OrchestratorRequest
import com.prueba.services.MarvelServiceInterface
import com.prueba.services.OrchestratorServiceInterface
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import jakarta.validation.Valid
import reactor.core.publisher.Mono

@Controller("/marvel")
class MarvelController(
    private val marvelServiceInterface: MarvelServiceInterface,
    private val orchestratorServiceInterface: OrchestratorServiceInterface
) {
    @Get("/comics")
    fun getComics(): Mono<List<ComicResponse>> {
        return this.marvelServiceInterface.getComics()
    }

    @Post("/orchestrator")
    fun orchestrator(@Valid @Body orchestratorRequest: OrchestratorRequest): Mono<OrchestratorResponse> {
        return this.orchestratorServiceInterface.resolve(orchestratorRequest)
    }
}