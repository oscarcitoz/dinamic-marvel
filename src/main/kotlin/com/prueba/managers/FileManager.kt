package com.prueba.managers

import com.prueba.configurations.entities.EndpointConfiguration
import com.prueba.containers.OrchestratorContainer
import io.micronaut.core.annotation.Order
import jakarta.inject.Singleton

@Singleton
@Order(1)
class FileManager(
    private val endpointConfiguration: EndpointConfiguration,
) : ManagerInterface {
    override fun apply(container: OrchestratorContainer) {
        container.api = this.endpointConfiguration.resolveConfiguration(container.orchestratorRequest!!.name)

        if (container.api?.dependsOn?.isNotEmpty() == true) {
            container.dependsApi = container.api?.dependsOn?.map {
                this.endpointConfiguration.resolveConfiguration(it)
            }
        }
    }
}