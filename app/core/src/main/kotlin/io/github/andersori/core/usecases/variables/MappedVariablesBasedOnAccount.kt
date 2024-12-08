package io.github.andersori.core.usecases.variables

import io.github.andersori.core.domain.Account
import io.github.andersori.core.ports.out.AccountInformation
import io.github.andersori.core.usecases.variables.unique.FindAccount
import io.github.andersori.core.usecases.variables.unique.Finder

class MappedVariablesBasedOnAccount(
    private val accountInformation: AccountInformation,
    vararg mappedFinders: Finder<Account, *>
) : MappedVariables<String, Account>(mappedVars = mappedFinders.associateBy { it.key() }) {

    override fun getNewRoot(): Finder<String, Account> = FindAccount(accountInformation)

}