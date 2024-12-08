package io.github.andersori.core.usecases.variables.unique

import io.github.andersori.core.domain.Client
import io.github.andersori.core.exceptions.ClientNotFound
import io.github.andersori.core.ports.out.ClientInformation
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindClient(
    private val clientInformation: ClientInformation,
    logger: Logger = CustomLoggerFactory.inline(FindClient::class.java)
) : Finder<String, Client>(key = "CLIENT", logger = logger) {
    @Throws(ClientNotFound::class)
    override suspend fun execute(identifier: String): Client {
        logger.info("buscando o ${key()} para o id $identifier")
        return clientInformation.find(identifier)
    }
}