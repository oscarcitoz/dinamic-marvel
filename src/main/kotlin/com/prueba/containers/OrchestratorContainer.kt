package com.prueba.containers

import com.prueba.configurations.entities.Api
import com.prueba.requests.OrchestratorRequest

class OrchestratorContainer {
    var orchestratorRequest: OrchestratorRequest? = null
    var api: Api? = null
    var wrapperForRequest: WrapperRequest? = null
}

data class WrapperRequest(val path: String, val params: Map<String, String>)