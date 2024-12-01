package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.domain.States
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindStates : Finder<Client, List<String>>(key = "STATES") {
    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(FindStates::class.java)
    }

    override fun execute(identifier: Client): List<String> {
        logger.info("buscando o ${key()} para o id $identifier")
        return States.getSender(identifier.cpf.replace(".", "").replace("-", "")[8].digitToInt()).names
    }
}