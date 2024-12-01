package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.ports.out.AccountInformation
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindAccount(
    private val accountInformation: AccountInformation
) : Finder<String, Account>(key = "ACCOUNT") {

    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(FindAccount::class.java)
    }

    override fun execute(identifier: String): Account {
        logger.info("buscando o ${key()} para o id $identifier")
        return accountInformation.find(identifier)
    }
}