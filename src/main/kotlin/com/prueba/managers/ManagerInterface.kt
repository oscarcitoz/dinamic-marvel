package com.prueba.managers

import com.prueba.containers.OrchestratorContainer

interface ManagerInterface {
    fun apply(container: OrchestratorContainer)
}