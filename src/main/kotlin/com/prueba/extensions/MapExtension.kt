package com.prueba.extensions

import java.util.zip.CRC32

fun Map<String, String>.generateCacheKey(): String {
    val sortedParams = this.entries.sortedBy { it.key }
    val concatenatedString = sortedParams.joinToString("&") { "${it.key}=${it.value}" }
    val crc = CRC32()
    crc.update(concatenatedString.toByteArray())

    return crc.value.toString()
}

fun Map<String?, Any?>.getValueByPath(path: String): String {
    val keys = path.split(".")
    var current: Any? = this

    for (key in keys) {
        current = if (current is Map<*, *>) {
            current[key]
        } else {
            return "0"
        }
    }
    return current.toString()
}