package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.exceptions.AccountNotFound
import io.github.andersori.products.core.ports.out.AccountInformation
import io.github.andersori.utils.Logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FindAccountTest {
    private lateinit var accountInformation: AccountInformation
    private lateinit var findAccount: FindAccount
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        accountInformation = mock<AccountInformation>()
        findAccount = FindAccount(accountInformation, logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindAccount(accountInformation) }
            .also { assertNotNull(it) }
    }

    @Test
    fun `conta encontrada`() {
        val id = UUID.randomUUID().toString()

        whenever(accountInformation.find(id)).thenReturn(
            Account(
                id = id,
                name = "TEST",
                active = true
            )
        )

        assertDoesNotThrow { findAccount.execute(identifier = id) }
            .also {
                assertEquals(id, it.id)
                assertEquals("TEST", it.name)
                assertTrue(it.active)
            }

        verify(logger, times(1)).info("buscando o ACCOUNT para o id $id")
        verify(accountInformation, times(1)).find(id)
    }

    @Test
    fun `key da service`() {
        assertEquals("ACCOUNT", findAccount.key())
        verifyNoInteractions(logger, accountInformation)
    }

    @Test
    fun `erro ao buscar conta`() {
        val id = UUID.randomUUID().toString()

        whenever(accountInformation.find(id)).thenThrow(AccountNotFound("Test"))

        assertThrows<AccountNotFound> { findAccount.execute(identifier = id) }
            .also {
                assertEquals("account_information", it.type())
                assertEquals("Test", it.message)
            }

        verify(logger, times(1)).info("buscando o ACCOUNT para o id $id")
        verify(accountInformation, times(1)).find(id)
    }
}