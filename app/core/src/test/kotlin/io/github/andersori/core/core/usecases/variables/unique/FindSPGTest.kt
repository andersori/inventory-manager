package io.github.andersori.core.core.usecases.variables.unique

import io.github.andersori.core.domain.Client
import io.github.andersori.core.exceptions.InvalidSPG
import io.github.andersori.core.usecases.variables.unique.FindSPG
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

class FindSPGTest {
    private lateinit var findSPG: FindSPG
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        findSPG = FindSPG(logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindSPG() }
            .also { assertNotNull(it) }
    }


    @Test
    fun `spg valido`() = runTest {
        val id = Client(
            cpf = "012.345.678-90"
        )

        assertDoesNotThrow { findSPG.execute(identifier = id) }
            .also { assertEquals("56", it) }

        verify(logger, times(1)).info("buscando o SPG para o id $id")
    }

    @Test
    fun `spg invalido`() = runTest {
        val id = Client(
            cpf = "012.345."
        )
        assertThrows<InvalidSPG> { findSPG.execute(identifier = id) }
            .also {
                assertEquals("invalid_spg", it.type())
                assertEquals("012345 nao tem 7 caracters no minimo", it.message)
            }

        verify(logger, times(1)).info("buscando o SPG para o id $id")
    }

    @Test
    fun `key da service`() {
        assertEquals("SPG", findSPG.key())
        verifyNoInteractions(logger)
    }
}