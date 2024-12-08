package io.github.andersori.core.core.adapters

import io.github.andersori.core.adapters.InMemoryClientInformation
import io.github.andersori.core.exceptions.ClientNotFound
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class InMemoryClientInformationTest {
    private lateinit var inMemoryClientInformation: InMemoryClientInformation

    @BeforeEach
    fun before() {
        inMemoryClientInformation = InMemoryClientInformation()
    }

    @Test
    fun `client encontrado`() = runTest {
        assertDoesNotThrow { inMemoryClientInformation.find("xpto") }
            .also { assertEquals("000.000.003-00", it.cpf) }
    }

    @Test
    fun `client nao encontrado 1`() = runTest {
        assertThrows<ClientNotFound> { inMemoryClientInformation.find("TEST") }
            .also {
                assertEquals("Test Client not found", it.message)
                assertEquals("client_not_found", it.type())
            }
    }

    @Test
    fun `client nao encontrado 2`() = runTest {
        assertThrows<ClientNotFound> { inMemoryClientInformation.find("test") }
            .also {
                assertEquals("Test Client not found", it.message)
                assertEquals("client_not_found", it.type())
            }
    }
}