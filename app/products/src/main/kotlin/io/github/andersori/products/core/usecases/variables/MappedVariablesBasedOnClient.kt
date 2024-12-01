package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.usecases.variables.unique.FindClient
import io.github.andersori.products.core.usecases.variables.unique.FindSPG
import io.github.andersori.products.core.usecases.variables.unique.FindStates

class MappedVariablesBasedOnClient(
    private val findSPG: FindSPG = FindSPG(),
    private val findStates: FindStates = FindStates(),
    vars: Map<String, Finder<Client, *>> = mapOf(
        findSPG.key() to findSPG,
        findStates.key() to findStates
    )
) : MappedVariables<String, Client>(vars) {

    override fun getNewRoot(): Finder<String, Client> = FindClient()

}