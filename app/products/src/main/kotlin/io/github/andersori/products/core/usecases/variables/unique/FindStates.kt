package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.domain.States
import io.github.andersori.products.core.exceptions.InvalidStates
import io.github.andersori.products.core.exceptions.StateNotFound
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindStates(
    logger: Logger = CustomLoggerFactory.inline(FindStates::class.java)
) : Finder<Client, List<String>>(key = "STATES", logger = logger) {
    companion object {
        private const val CHAR_POSITION = 8
    }

    @Throws(StateNotFound::class, InvalidStates::class)
    override fun execute(identifier: Client): List<String> {
        logger.info("buscando o ${key()} para o id $identifier")
        val cpfWithOnlyDigits = identifier.cpf.filter { it.isDigit() }
        if (cpfWithOnlyDigits.length >= CHAR_POSITION + 1) {
            return States.getSender(cpfWithOnlyDigits[CHAR_POSITION].digitToInt()).names
        }
        throw InvalidStates("$cpfWithOnlyDigits nao tem o caracter na posicao $CHAR_POSITION")
    }
}