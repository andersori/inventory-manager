package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.exceptions.InvalidSPG
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindSPG(
    logger: Logger = CustomLoggerFactory.inline(FindSPG::class.java)
) : Finder<Client, String>(key = "SPG", logger = logger) {
    companion object {
        private const val MIN_LENGTH = 7
    }

    @Throws(InvalidSPG::class)
    override fun execute(identifier: Client): String {
        logger.info("buscando o ${key()} para o id $identifier")
        val cpfWithOnlyDigits = identifier.cpf.filter { it.isDigit() }
        if (cpfWithOnlyDigits.length >= MIN_LENGTH) {
            return cpfWithOnlyDigits.substring(5, 7)
        }
        throw InvalidSPG("$cpfWithOnlyDigits nao tem $MIN_LENGTH caracters no minimo")
    }
}