package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.ports.ClientInformation
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindClient(
    private val clientInformation: ClientInformation
) : Finder<String, Client>(key = "CLIENT") {

    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(FindClient::class.java)
    }

    override fun execute(identifier: String): Client {
        logger.info("buscando o ${key()} para o id $identifier")
        return clientInformation.find(identifier)
    }
}