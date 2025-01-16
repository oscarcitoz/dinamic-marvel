package com.prueba.externals.marvel.clients

import com.prueba.externals.marvel.responses.BaseResponse
import com.prueba.externals.marvel.responses.ComicResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

@Client("marvel")
interface MarvelClient {
    @Get("/v1/public/comics")
    fun getComics(): Mono<BaseResponse<ComicResponse>>

    @Get("/{path}")
    fun orchestratorMarvel(
        @PathVariable path: String,
        @QueryValue params: Map<String, String>
    ): Mono<BaseResponse<ComicResponse>>
}