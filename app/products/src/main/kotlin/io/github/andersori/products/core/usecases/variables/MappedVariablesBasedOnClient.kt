package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.ports.out.ClientInformation
import io.github.andersori.products.core.usecases.variables.unique.FindClient
import io.github.andersori.products.core.usecases.variables.unique.FindSPG
import io.github.andersori.products.core.usecases.variables.unique.FindStates

class MappedVariablesBasedOnClient(
    private val clientInformation: ClientInformation,
    vars: Map<String, Finder<Client, *>> = mapOf(
        FindSPG().let { it.key() to it },
        FindStates().let { it.key() to it }
    )
) : MappedVariables<String, Client>(mappedVars = vars) {

    override fun getNewRoot(): Finder<String, Client> = FindClient(clientInformation)

}