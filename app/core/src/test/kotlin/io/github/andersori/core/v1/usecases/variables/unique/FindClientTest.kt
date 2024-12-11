package io.github.andersori.core.v1.usecases.variables.unique

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.exceptions.ClientNotFound
import io.github.andersori.core.v1.ports.out.ClientInformation
import io.github.andersori.utils.Logger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FindClientTest {
    private lateinit var clientInformation: ClientInformation
    private lateinit var findClient: FindClient
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        clientInformation = mock<ClientInformation>()
        findClient = FindClient(clientInformation, logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindClient(clientInformation) }
            .also { assertNotNull(it) }
    }

    @Test
    fun `cliente encontrado`() = runTest {
        val id = UUID.randomUUID().toString()
        val cpf = "000.000.000-00"
        whenever(clientInformation.find(id)).thenReturn(
            Client(
                cpf = cpf
            )
        )
        assertDoesNotThrow { findClient.execute(id) }
            .also {
                assertEquals(cpf, it.cpf)
            }
        verify(logger, times(1)).info("buscando o CLIENT para o id $id")
        verify(clientInformation, times(1)).find(id)
    }

    @Test
    fun `key da service`() {
        assertEquals("CLIENT", findClient.key())
        verifyNoInteractions(logger, clientInformation)
    }

    @Test
    fun `erro ao buscar cliente`() = runTest {
        val id = UUID.randomUUID().toString()
        whenever(clientInformation.find(id)).thenThrow(ClientNotFound("Test"))
        assertThrows<ClientNotFound> { findClient.execute(id) }
            .also {
                assertEquals("client_not_found", it.type())
                assertEquals("Test", it.message)
            }
        verify(logger, times(1)).info("buscando o CLIENT para o id $id")
        verify(clientInformation, times(1)).find(id)
    }
}