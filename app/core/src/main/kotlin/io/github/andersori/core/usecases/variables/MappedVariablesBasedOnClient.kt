package io.github.andersori.core.usecases.variables

import io.github.andersori.core.domain.Client
import io.github.andersori.core.ports.out.ClientInformation
import io.github.andersori.core.usecases.variables.unique.FindClient
import io.github.andersori.core.usecases.variables.unique.Finder

class MappedVariablesBasedOnClient(
    private val clientInformation: ClientInformation,
    vararg mappedFinders: Finder<Client, *>
) : MappedVariables<String, Client>(mappedVars = mappedFinders.associateBy { it.key() }) {

    override fun getNewRoot(): Finder<String, Client> = FindClient(clientInformation)

}