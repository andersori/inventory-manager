package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindSPG : Finder<Client, String>(key = "SPG") {
    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(FindSPG::class.java)
    }

    override fun execute(identifier: Client): String {
        logger.info("buscando o ${key()} para o id $identifier")
        return identifier.cpf.replace(".", "").replace("-", "").substring(5, 7)
    }
}