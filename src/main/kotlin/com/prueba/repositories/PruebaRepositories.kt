package com.prueba.repositories

import com.prueba.models.Prueba
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface PruebaRepositories: CrudRepository<Prueba, Long> {
}