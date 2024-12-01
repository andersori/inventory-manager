package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.usecases.variables.unique.FindAccount
import io.github.andersori.products.core.usecases.variables.unique.FindActiveUser
import io.github.andersori.products.core.usecases.variables.unique.FindTestUser

class MappedVariablesBasedOnAccount(
    private val findTestUser: FindTestUser = FindTestUser(),
    private val findActiveUser: FindActiveUser = FindActiveUser(),
    vars: Map<String, Finder<Account, *>> = mapOf(
        findTestUser.key() to findTestUser,
        findActiveUser.key() to findActiveUser
    )
) : MappedVariables<String, Account>(vars) {

    override fun getNewRoot(): Finder<String, Account> = FindAccount()

}