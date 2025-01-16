package com.prueba.services

import com.prueba.externals.marvel.clients.MarvelClient
import com.prueba.externals.marvel.responses.ComicResponse
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Singleton
class MarvelService(
    private val marvelClient: MarvelClient
) : MarvelServiceInterface {
    override fun getComics(): Mono<List<ComicResponse>> {
        return this.marvelClient.getComics().map {
            it.data?.results
        }
    }
}