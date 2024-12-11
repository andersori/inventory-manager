package io.github.andersori.core.v1.adapters

import io.github.andersori.core.v1.exceptions.AccountNotFound
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class InMemoryAccountInformationTest {
    private lateinit var inMemoryAccountInformation: InMemoryAccountInformation


    @BeforeEach
    fun before() {
        inMemoryAccountInformation = InMemoryAccountInformation()
    }

    @Test
    fun `account encontrado`() = runTest {
        assertDoesNotThrow { inMemoryAccountInformation.find("xpto") }
            .also {
                assertEquals("xpto", it.id)
                assertEquals("TEST", it.name)
                assertEquals(true, it.active)
            }
    }

    @Test
    fun `account nao encontrado 1`() = runTest {
        assertThrows<AccountNotFound> { inMemoryAccountInformation.find("TEST") }
            .also {
                assertEquals("Test Account not found", it.message)
                assertEquals("account_not_found", it.type())
            }
    }

    @Test
    fun `account nao encontrado 2`() = runTest {
        assertThrows<AccountNotFound> { inMemoryAccountInformation.find("test") }
            .also {
                assertEquals("Test Account not found", it.message)
                assertEquals("account_not_found", it.type())
            }
    }
}