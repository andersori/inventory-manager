package io.github.andersori.core.usecases.variables.unique

import io.github.andersori.core.domain.Account
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindFakeAccount(
    logger: Logger = CustomLoggerFactory.inline(FindFakeAccount::class.java)
) : Finder<Account, Boolean>(key = "FAKE_ACCOUNT", logger = logger) {
    override suspend fun execute(identifier: Account): Boolean {
        logger.info("buscando o ${key()} para o id $identifier")
        return identifier.name.equals("FAKE", ignoreCase = true)
    }
}