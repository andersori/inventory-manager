package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindTestAccount : Finder<Account, Boolean>(key = "TEST_ACCOUNT") {
    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(FindTestAccount::class.java)
    }

    override fun execute(identifier: Account): Boolean {
        logger.info("buscando o ${key()} para o id $identifier")
        return identifier.name.equals("TEST", ignoreCase = true)
    }
}