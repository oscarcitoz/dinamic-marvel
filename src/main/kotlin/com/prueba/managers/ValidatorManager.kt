package com.prueba.managers

import com.prueba.configurations.entities.Parameter
import com.prueba.containers.OrchestratorContainer
import io.micronaut.core.annotation.Order
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Singleton

@Singleton
@Order(2)
class ValidatorManager : ManagerInterface {
    companion object {
        private const val BOOLEAN = "boolean"
        private const val INT = "int"
        private const val STRING = "string"
    }
    override fun apply(container: OrchestratorContainer) {
        val request = container.orchestratorRequest!!
        val configuration = container.api!!.configuration

        configuration.parameters.forEach { parameterConfig ->
            val parameterForValidate = request.parameters.firstOrNull {
                it.key == parameterConfig.key
            }

            if (parameterConfig.required == true && parameterForValidate == null) throw HttpStatusException(
                HttpStatus.BAD_REQUEST,
                "parameter ${parameterConfig.key} is required"
            ) else if (parameterForValidate != null) {
                when (parameterConfig.type) {
                    BOOLEAN -> validateBoolean(parameterConfig, parameterForValidate)
                    INT -> validateInt(parameterConfig, parameterForValidate)
                    STRING -> validateString(parameterConfig, parameterForValidate)
                }
            }
        }
    }

    private fun validateBoolean(parameterConfig: Parameter, parameterForValidate: com.prueba.requests.Parameter) {
        if (parameterForValidate.value !in listOf("true", "false")) {
            throwBadRequestException(parameterConfig, "should be boolean")
        }
    }

    private fun validateInt(parameterConfig: Parameter, parameterForValidate: com.prueba.requests.Parameter) {
        try {
            parameterForValidate.value.toInt()
        } catch (e: NumberFormatException) {
            throwBadRequestException(parameterConfig, "should be a number")
        }
    }

    private fun validateString(parameterConfig: Parameter, parameterForValidate: com.prueba.requests.Parameter) {
        parameterConfig.possibleValues?.let {
            if (parameterForValidate.value !in it) {
                throwBadRequestException(parameterConfig, "should be one of ${it.joinToString(", ")}")
            }
        }
    }

    private fun throwBadRequestException(parameterConfig: Parameter, errorMessage: String) {
        throw HttpStatusException(
            HttpStatus.BAD_REQUEST,
            "parameter ${parameterConfig.key} $errorMessage"
        )
    }
}