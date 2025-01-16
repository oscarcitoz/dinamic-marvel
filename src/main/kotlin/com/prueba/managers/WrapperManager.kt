package com.prueba.managers

import com.prueba.containers.OrchestratorContainer
import com.prueba.containers.WrapperRequest
import com.prueba.extensions.replaceUrlParameters
import io.micronaut.core.annotation.Order
import jakarta.inject.Singleton

@Singleton
@Order(3)
class WrapperManager : ManagerInterface {
    override fun apply(container: OrchestratorContainer) {
        val api = container.api!!
        val parameters = container.orchestratorRequest!!.parametersToMap()

        container.wrapperForRequest = WrapperRequest(
            path = api.configuration.url.replaceUrlParameters(parameters),
            params = parameters
        )
    }
}