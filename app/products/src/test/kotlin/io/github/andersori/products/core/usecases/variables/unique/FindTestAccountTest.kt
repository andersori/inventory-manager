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

class FindTestAccountTest {
    private lateinit var findTestAccount: FindTestAccount
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        findTestAccount = FindTestAccount(logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindTestAccount() }
            .also { assertNotNull(it) }
    }

    private fun testFakeAccount(id: Account) {
        assertDoesNotThrow { findTestAccount.execute(identifier = id) }
            .also { assertTrue(it) }

        verify(logger, times(1)).info("buscando o TEST_ACCOUNT para o id $id")
    }

    @Test
    fun `eh conta de test`() {
        testFakeAccount(
            Account(
                id = UUID.randomUUID().toString(),
                name = "test",
                active = true
            )
        )

        testFakeAccount(
            Account(
                id = UUID.randomUUID().toString(),
                name = "TEST",
                active = true
            )
        )
    }

    @Test
    fun `nao eh conta de test`() {
        val id = Account(
            id = UUID.randomUUID().toString(),
            name = "temp",
            active = true
        )

        assertDoesNotThrow { findTestAccount.execute(identifier = id) }
            .also { assertFalse(it) }

        verify(logger, times(1)).info("buscando o TEST_ACCOUNT para o id $id")
    }

    @Test
    fun `key da service`() {
        assertEquals("TEST_ACCOUNT", findTestAccount.key())
        verifyNoInteractions(logger)
    }

}