package io.github.andersori.core.core.usecases.variables.unique

import io.github.andersori.core.domain.Account
import io.github.andersori.core.usecases.variables.unique.FindFakeAccount
import io.github.andersori.utils.Logger
import kotlinx.coroutines.test.runTest
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

class FindFakeAccountTest {
    private lateinit var findFakeAccount: FindFakeAccount
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
        findFakeAccount = FindFakeAccount(logger)
    }

    @Test
    fun testWithoutCustomLogger() {
        assertDoesNotThrow { FindFakeAccount() }
            .also { assertNotNull(it) }
    }

    private suspend fun testFakeAccount(id: Account) {
        assertDoesNotThrow { findFakeAccount.execute(identifier = id) }
            .also { assertTrue(it) }

        verify(logger, times(1)).info("buscando o FAKE_ACCOUNT para o id $id")
    }

    @Test
    fun `eh conta fake`() = runTest {
        testFakeAccount(
            Account(
                id = UUID.randomUUID().toString(),
                name = "FAKE",
                active = true
            )
        )

        testFakeAccount(
            Account(
                id = UUID.randomUUID().toString(),
                name = "fake",
                active = true
            )
        )
    }

    @Test
    fun `nao eh conta fake`() = runTest {
        val id = Account(
            id = UUID.randomUUID().toString(),
            name = "test",
            active = true
        )

        assertDoesNotThrow { findFakeAccount.execute(identifier = id) }
            .also { assertFalse(it) }

        verify(logger, times(1)).info("buscando o FAKE_ACCOUNT para o id $id")
    }

    @Test
    fun `key da service`() {
        assertEquals("FAKE_ACCOUNT", findFakeAccount.key())
        verifyNoInteractions(logger)
    }

}