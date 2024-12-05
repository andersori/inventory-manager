package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.ports.out.AccountInformation
import io.github.andersori.products.core.usecases.variables.unique.FindAccount

class MappedVariablesBasedOnAccount(
    private val accountInformation: AccountInformation,
    vararg mappedFinders: Finder<Account, *>
) : MappedVariables<String, Account>(mappedVars = mappedFinders.associateBy { it.key() }) {

    override fun getNewRoot(): Finder<String, Account> = FindAccount(accountInformation)

}