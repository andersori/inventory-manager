package io.github.andersori.core.v1.usecases.variables.unique

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.exceptions.InvalidStates
import io.github.andersori.core.v1.exceptions.StateNotFound
import io.github.andersori.utils.Logger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FindStatesTest {
    private lateinit var findStates: FindStates
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        findStates = FindStates(logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindStates() }
            .also { assertNotNull(it) }
    }

    @Test
    fun `state encontrado`() = runTest {
        val id = Client(
            cpf = "000.000.003-00"
        )

        assertDoesNotThrow { findStates.execute(identifier = id) }
            .also { assertEquals(listOf("Ceará", "Maranhão", "Piauí"), it) }

        verify(logger, times(1)).info("buscando o STATES para o id $id")
    }

    @Test
    fun `state nao encontrado`() = runTest {
        val id = Client(
            cpf = "000.000.000-00"
        )

        assertThrows<StateNotFound> { findStates.execute(identifier = id) }
            .also {
                assertEquals("enum_states", it.type())
                assertEquals("Identificador 0 não encontrado", it.message)
            }

        verify(logger, times(1)).info("buscando o STATES para o id $id")
    }

    @Test
    fun `state invalido`() = runTest {
        val id = Client(
            cpf = "000.000.00"
        )

        assertThrows<InvalidStates> { findStates.execute(identifier = id) }
            .also {
                assertEquals("invalid_states", it.type())
                assertEquals("00000000 nao tem o caracter na posicao 8", it.message)
            }

        verify(logger, times(1)).info("buscando o STATES para o id $id")
    }

    @Test
    fun `key da service`() {
        assertEquals("STATES", findStates.key())
        verifyNoInteractions(logger)
    }
}