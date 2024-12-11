package io.github.andersori.core.v1.usecases.variables.unique

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.domain.States
import io.github.andersori.core.v1.exceptions.InvalidStates
import io.github.andersori.core.v1.exceptions.StateNotFound
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger

class FindStates(
    logger: Logger = CustomLoggerFactory.inline(FindStates::class.java)
) : Finder<Client, List<String>>(key = "STATES", logger = logger) {
    companion object {
        private const val CHAR_POSITION = 8
    }

    @Throws(StateNotFound::class, InvalidStates::class)
    override suspend fun execute(identifier: Client): List<String> {
        logger.info("buscando o ${key()} para o id $identifier")
        val cpfWithOnlyDigits = identifier.cpf.filter { it.isDigit() }
        if (cpfWithOnlyDigits.length >= CHAR_POSITION + 1) {
            return States.getSender(cpfWithOnlyDigits[CHAR_POSITION].digitToInt()).names
        }
        throw InvalidStates("$cpfWithOnlyDigits nao tem o caracter na posicao $CHAR_POSITION")
    }
}