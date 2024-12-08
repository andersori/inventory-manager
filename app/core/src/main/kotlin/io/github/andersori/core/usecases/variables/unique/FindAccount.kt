package io.github.andersori.core.usecases.variables.unique

import io.github.andersori.core.domain.Account
import io.github.andersori.core.exceptions.AccountNotFound
import io.github.andersori.core.ports.out.AccountInformation
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindAccount(
    private val accountInformation: AccountInformation,
    logger: Logger = CustomLoggerFactory.inline(FindAccount::class.java)
) : Finder<String, Account>(key = "ACCOUNT", logger = logger) {
    @Throws(AccountNotFound::class)
    override suspend fun execute(identifier: String): Account {
        logger.info("buscando o ${key()} para o id $identifier")
        return accountInformation.find(identifier)
    }
}