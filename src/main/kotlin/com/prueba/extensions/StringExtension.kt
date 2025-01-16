package com.prueba.extensions

fun String.replaceUrlParameters(parameters: Map<String, String>): String {
    var url = this
    val regex = "\\{(.*?)\\}".toRegex()

    regex.findAll(this).forEach { matchResult ->
        val key = matchResult.groupValues[1]
        val value = parameters[key]
        if (value != null) {
            url = url.replace(matchResult.value, value)
        }
    }
    return url
}