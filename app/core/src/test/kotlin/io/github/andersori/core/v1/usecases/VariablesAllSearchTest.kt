package io.github.andersori.core.v1.usecases

import io.github.andersori.core.v1.exceptions.ClientNotFound
import io.github.andersori.core.v1.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.core.v1.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.core.v1.usecases.variables.unique.FindClient
import io.github.andersori.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VariablesAllSearchTest {
    private lateinit var mappedVariablesBasedOnClient: MappedVariablesBasedOnClient
    private lateinit var mappedVariablesBasedOnAccount: MappedVariablesBasedOnAccount
    private lateinit var variablesAllSearch: VariablesAllSearch
    private lateinit var logger: Logger
    private lateinit var rootFindClient: FindClient
    private lateinit var rootFindAccount: io.github.andersori.core.v1.usecases.variables.unique.FindAccount

    @BeforeEach
    fun before() {
        rootFindClient = mock<FindClient>()
        rootFindAccount = mock<io.github.andersori.core.v1.usecases.variables.unique.FindAccount>()
        logger = mock<Logger>()
    }

    @Test
    fun testNewInstanceWithoutCustomLogger() {
        mappedVariablesBasedOnClient = mock<MappedVariablesBasedOnClient>()
        mappedVariablesBasedOnAccount = mock<MappedVariablesBasedOnAccount>()
        assertDoesNotThrow { VariablesAllSearch(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount) }
            .also { assertNotNull(it) }
    }

    @Test
    fun testSearch() = runTest {
        val id = "1234"
        whenever(rootFindClient.handler(eq(id), eq(true), eq(Dispatchers.Default))).thenReturn(
            mapOf(
                "a" to 1,
                "b" to 2
            )
        )
        whenever(rootFindAccount.handler(eq(id), eq(true), eq(Dispatchers.Default))).thenReturn(
            mapOf(
                "c" to 3,
                "d" to 4
            )
        )
        mappedVariablesBasedOnClient = mock<MappedVariablesBasedOnClient> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFindClient
        }
        mappedVariablesBasedOnAccount = mock<MappedVariablesBasedOnAccount> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFindAccount
        }
        variablesAllSearch =
            VariablesAllSearch(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount, logger = logger)
        assertDoesNotThrow { variablesAllSearch.search("1234", "a", "b", "c", "d") }
            .also {
                assertEquals(
                    mapOf(
                        "a" to 1,
                        "b" to 2,
                        "c" to 3,
                        "d" to 4
                    ), it
                )
            }

        verify(mappedVariablesBasedOnClient, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnClient, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(logger, times(1)).info("waiting all...")
    }

    @Test
    fun testSearchWithNoVarsFound() = runTest {
        val id = "1234"
        whenever(rootFindClient.handler(eq(id), eq(true), eq(Dispatchers.Default))).thenReturn(
            mapOf(
                "a" to 1,
                "b" to 2
            )
        )
        whenever(rootFindAccount.handler(eq(id), eq(true), eq(Dispatchers.Default))).thenReturn(
            mapOf(
                "c" to 3,
                "d" to 4
            )
        )
        mappedVariablesBasedOnClient = mock<MappedVariablesBasedOnClient> {
            on { configRootHandler(anyVararg<String>()) } doReturn null
        }
        mappedVariablesBasedOnAccount = mock<MappedVariablesBasedOnAccount> {
            on { configRootHandler(anyVararg<String>()) } doReturn null
        }
        variablesAllSearch =
            VariablesAllSearch(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount, logger = logger)
        assertDoesNotThrow { variablesAllSearch.search("1234", "a", "b", "c", "d") }
            .also { assertEquals(emptyMap<String, Any>(), it) }

        verify(mappedVariablesBasedOnClient, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnClient, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(logger, times(1)).info("waiting all...")
    }

    @Test
    fun testSearchWithError1() = runTest {
        val id = "1234"
        whenever(rootFindClient.handler(eq(id), eq(true), eq(Dispatchers.Default))).thenReturn(
            mapOf(
                "a" to 1,
                "b" to 2
            )
        )
        whenever(rootFindAccount.handler(eq(id), eq(true), eq(Dispatchers.Default))).thenThrow(ClientNotFound("Test"))
        mappedVariablesBasedOnClient = mock<MappedVariablesBasedOnClient> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFindClient
        }
        mappedVariablesBasedOnAccount = mock<MappedVariablesBasedOnAccount> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFindAccount
        }
        variablesAllSearch =
            VariablesAllSearch(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount, logger = logger)
        assertThrows<ClientNotFound> { variablesAllSearch.search("1234", "a", "b", "c", "d") }
            .also {
                assertEquals("Test", it.message)
                assertEquals("client_not_found", it.type())
            }

        verify(mappedVariablesBasedOnClient, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnClient, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(logger, times(0)).info("waiting all...")
    }

    @Test
    fun testSearchWithError2() = runTest {
        mappedVariablesBasedOnClient = mock<MappedVariablesBasedOnClient> {
            on { configRootHandler(anyVararg<String>()) } doThrow RuntimeException("Test")
        }
        mappedVariablesBasedOnAccount = mock<MappedVariablesBasedOnAccount> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFindAccount
        }
        variablesAllSearch =
            VariablesAllSearch(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount, logger = logger)
        assertThrows<RuntimeException> { variablesAllSearch.search("1234", "a", "b", "c", "d") }
            .also {
                assertEquals("Test", it.message)
            }

        verify(mappedVariablesBasedOnClient, times(1))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(0))
            .configRootHandler(eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnClient, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(mappedVariablesBasedOnAccount, times(0))
            .configRootHandler(any<Boolean>(), eq("a"), eq("b"), eq("c"), eq("d"))
        verify(logger, times(0)).info("waiting all...")
    }
}