package com.prueba.services

import com.prueba.externals.marvel.responses.ComicResponse
import reactor.core.publisher.Mono

interface MarvelServiceInterface {
    fun getComics(): Mono<List<ComicResponse>>
}