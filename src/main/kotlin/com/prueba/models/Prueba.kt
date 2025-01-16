package com.prueba.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import io.micronaut.serde.annotation.Serdeable
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.Type
import java.time.LocalDateTime

@Serdeable
@Entity
@Table(name = "pruebas")
class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pruebas_id_seq_generator")
    @SequenceGenerator(name = "pruebas_id_seq_generator", sequenceName = "pruebas_id_seq", allocationSize = 1)
    open var id: Long? = null

    @Column( name = "name")
    var name: String = "pepe"

    @Column(name = "created_at")
    @field:JsonProperty("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Type(value = JsonBinaryType::class)
    @Column(name = "metadata", columnDefinition = "jsonb")
    var metadata: Metadata? = null
}