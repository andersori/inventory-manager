package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.ports.out.AccountInformation
import io.github.andersori.products.core.usecases.variables.unique.FindAccount
import io.github.andersori.products.core.usecases.variables.unique.FindActiveAccount
import io.github.andersori.products.core.usecases.variables.unique.FindFakeAccount
import io.github.andersori.products.core.usecases.variables.unique.FindTestAccount

class MappedVariablesBasedOnAccount(
    private val accountInformation: AccountInformation,
) : MappedVariables<String, Account>(
    mappedVars = mapOf(
        FindTestAccount().let { it.key() to it },
        FindActiveAccount().let { it.key() to it },
        FindFakeAccount().let { it.key() to it }
    )
) {

    override fun getNewRoot(): Finder<String, Account> = FindAccount(accountInformation)

}