package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.usecases.variables.unique.FindAccount
import io.github.andersori.products.core.usecases.variables.unique.FindActiveUser
import io.github.andersori.products.core.usecases.variables.unique.FindTestUser

class MappedVariablesBasedOnAccount(
    private val findTestUser: FindTestUser = FindTestUser(),
    private val findActiveUser: FindActiveUser = FindActiveUser(),
    vars: Map<String, Finder<*, *>> = mapOf(
        findTestUser.key() to findTestUser,
        findActiveUser.key() to findActiveUser
    )
) : MappedVariables<String, Account>(vars) {

    override fun getNewRoot(): Finder<String, Account> = FindAccount()

    override fun addHandlers(
        finder: Finder<*, *>?,
        key: String,
        root: Finder<String, Account>
    ): Pair<String, Finder<String, Account>?> = when (finder?.key()) {
        findTestUser.key() -> {
            key to root.addNext(findTestUser)
        }

        findActiveUser.key() -> {
            key to root.addNext(findActiveUser)
        }

        else -> {
            key to null
        }
    }

}