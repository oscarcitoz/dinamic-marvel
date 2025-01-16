package com.prueba.extensions

import java.util.zip.CRC32

fun Map<String, String>.generateCacheKey(): String {
    val sortedParams = this.entries.sortedBy { it.key }
    val concatenatedString = sortedParams.joinToString("&") { "${it.key}=${it.value}" }
    val crc = CRC32()
    crc.update(concatenatedString.toByteArray())

    return crc.value.toString()
}