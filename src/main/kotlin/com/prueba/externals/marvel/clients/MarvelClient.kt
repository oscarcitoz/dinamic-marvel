package com.prueba.externals.marvel.clients

import com.prueba.externals.marvel.responses.BaseResponse
import com.prueba.externals.marvel.responses.ComicResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

@Client("marvel")
interface MarvelClient {
    @Get("/v1/public/comics")
    fun getComics(): Mono<BaseResponse<ComicResponse>>
}