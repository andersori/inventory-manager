package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.ports.out.ClientInformation
import io.github.andersori.products.core.usecases.variables.unique.FindClient
import io.github.andersori.products.core.usecases.variables.unique.FindSPG
import io.github.andersori.products.core.usecases.variables.unique.FindStates

class MappedVariablesBasedOnClient(
    private val clientInformation: ClientInformation,
    vararg mappedFinders: Finder<Client, *>
) : MappedVariables<String, Client>(mappedVars = mappedFinders.associateBy { it.key() }) {

    override fun getNewRoot(): Finder<String, Client> = FindClient(clientInformation)

}