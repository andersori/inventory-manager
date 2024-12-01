package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Account
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import java.util.UUID

class FindAccount : Finder<String, Account>(key = "ACCOUNT") {

    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(FindAccount::class.java)
    }

    override fun execute(identifier: String): Account {
        logger.info("buscando o ${key()} para o id $identifier")
        return Account(
            id = UUID.randomUUID().toString(),
            name = "TEST",
            active = true
        )
    }
}