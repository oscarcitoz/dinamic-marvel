package com.prueba.externals.marvel.responses

import io.micronaut.core.annotation.Introspected
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@Introspected
class ComicResponse {
    val id: Int = 0
    val title: String = ""
}