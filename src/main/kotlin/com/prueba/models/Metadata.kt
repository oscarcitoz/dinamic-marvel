package com.prueba.models

import io.micronaut.serde.annotation.Serdeable

@Serdeable
 class Metadata {
    var clave1: String = ""
    var clave2: Int = 0
    var clave3: List<Int> = listOf()
 }