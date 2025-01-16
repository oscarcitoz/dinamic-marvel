package com.prueba.externals.marvel.filters

import io.micronaut.context.annotation.Value
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.ClientFilter
import io.micronaut.http.annotation.RequestFilter
import org.apache.commons.codec.digest.DigestUtils

@ClientFilter("/v1/**")
class MarvelClientFilter(
    @Value("\${marvel.api-key}") private val apiKey: String,
    @Value("\${marvel.private-key}") private val privateKey: String,
    @Value("\${micronaut.http.services.marvel.url}") private val marvelUrl: String,
) {

    @RequestFilter
    fun doFilter(request: MutableHttpRequest<*>) {
        if (request.uri.toString().startsWith(this.marvelUrl)) {
            val ts = System.currentTimeMillis().toString()
            val hash = DigestUtils.md5Hex("$ts${this.privateKey}${this.apiKey}")

            request.uri { uri ->
                uri.queryParam("ts", ts)
                uri.queryParam("apikey", this.apiKey)
                uri.queryParam("hash", hash)
            }
        }
    }
}