package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.exceptions.ClientNotFound
import io.github.andersori.products.core.ports.out.ClientInformation
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindClient(
    private val clientInformation: ClientInformation,
    logger: Logger = CustomLoggerFactory.inline(FindClient::class.java)
) : Finder<String, Client>(key = "CLIENT", logger = logger) {
    @Throws(ClientNotFound::class)
    override fun execute(identifier: String): Client {
        logger.info("buscando o ${key()} para o id $identifier")
        return clientInformation.find(identifier)
    }
}