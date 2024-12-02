package io.github.andersori.products.core.usecases.variables.unique

import io.github.andersori.products.core.domain.Account
import io.github.andersori.utils.Logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FindActiveAccountTest {
    private lateinit var findActiveAccount: FindActiveAccount
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        findActiveAccount = FindActiveAccount(logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindActiveAccount() }
            .also { assertNotNull(it) }
    }

    @Test
    fun `eh conta ativa`() {
        val id = Account(
            id = UUID.randomUUID().toString(),
            name = "Test",
            active = true
        )

        assertDoesNotThrow { findActiveAccount.execute(identifier = id) }
            .also { assertTrue(it) }

        verify(logger, times(1)).info("buscando o ACTIVE_ACCOUNT para o id $id")
    }

    @Test
    fun `nao eh conta ativa`() {
        val id = Account(
            id = UUID.randomUUID().toString(),
            name = "Test",
            active = false
        )

        assertDoesNotThrow { findActiveAccount.execute(identifier = id) }
            .also { assertFalse(it) }

        verify(logger, times(1)).info("buscando o ACTIVE_ACCOUNT para o id $id")
    }

    @Test
    fun `key da service`() {
        assertEquals("ACTIVE_ACCOUNT", findActiveAccount.key())
        verifyNoInteractions(logger)
    }

}