package io.github.andersori.core.v1.usecases.variables.unique

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.exceptions.ClientNotFound
import io.github.andersori.core.v1.ports.out.ClientInformation
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