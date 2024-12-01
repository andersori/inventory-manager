package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.usecases.variables.unique.FindAccount
import io.github.andersori.products.core.usecases.variables.unique.FindActiveAccount
import io.github.andersori.products.core.usecases.variables.unique.FindTestAccount

class MappedVariablesBasedOnAccount(
    private val findTestAccount: FindTestAccount = FindTestAccount(),
    private val findActiveAccount: FindActiveAccount = FindActiveAccount(),
    vars: Map<String, Finder<Account, *>> = mapOf(
        findTestAccount.key() to findTestAccount,
        findActiveAccount.key() to findActiveAccount
    )
) : MappedVariables<String, Account>(vars) {

    override fun getNewRoot(): Finder<String, Account> = FindAccount()

}