package io.github.andersori.core.v1.usecases.variables

import io.github.andersori.core.v1.domain.Account
import io.github.andersori.core.v1.ports.out.AccountInformation
import io.github.andersori.core.v1.usecases.variables.unique.Finder

class MappedVariablesBasedOnAccount(
    private val accountInformation: AccountInformation,
    vararg mappedFinders: Finder<Account, *>
) : MappedVariables<String, Account>(mappedVars = mappedFinders.associateBy { it.key() }) {

    override fun getNewRoot(): Finder<String, Account> =
        io.github.andersori.core.v1.usecases.variables.unique.FindAccount(accountInformation)

}