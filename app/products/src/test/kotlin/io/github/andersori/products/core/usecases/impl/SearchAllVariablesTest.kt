package io.github.andersori.products.core.usecases.impl

import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.products.core.usecases.variables.unique.FindAccount
import io.github.andersori.products.core.usecases.variables.unique.FindClient
import io.github.andersori.utils.Logger
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SearchAllVariablesTest {
    private lateinit var mappedVariablesBasedOnClient: MappedVariablesBasedOnClient
    private lateinit var mappedVariablesBasedOnAccount: MappedVariablesBasedOnAccount
    private lateinit var searchAllVariables: SearchAllVariables
    private lateinit var logger: Logger
    private lateinit var rootFinderClient: FindClient
    private lateinit var rootFindAccount: FindAccount

    @BeforeEach
    fun before() {
        rootFinderClient = mock<FindClient>()
        rootFindAccount = mock<FindAccount>()
        mappedVariablesBasedOnClient = mock<MappedVariablesBasedOnClient> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFinderClient
            on { configRootHandler(any<Boolean>(), anyVararg()) } doReturn rootFinderClient
        }
        mappedVariablesBasedOnAccount = mock<MappedVariablesBasedOnAccount> {
            on { configRootHandler(anyVararg<String>()) } doReturn rootFindAccount
            on { configRootHandler(any<Boolean>(), anyVararg()) } doReturn rootFindAccount
        }
        logger = mock<Logger>()
        searchAllVariables = SearchAllVariables(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount, logger=logger)
    }

    @Test
    fun testNewInstanceWithoutCustomLogger() {
        assertDoesNotThrow { SearchAllVariables(mappedVariablesBasedOnClient, mappedVariablesBasedOnAccount) }
            .also { assertNotNull(it) }
    }

    @Test
    fun testSearch() {
        val id = "1234"
        whenever(rootFinderClient.handler(eq(id), any())).thenReturn(
            mapOf(
                "a" to 1,
                "b" to 2
            )
        )
        whenever(rootFindAccount.handler(eq(id), any())).thenReturn(
            mapOf(
                "c" to 3,
                "d" to 4
            )
        )
        assertDoesNotThrow { searchAllVariables.search("1234", "a", "b", "c", "d") }
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
            .configRootHandler(
                eq(true),
                eq("a"),
                eq("b"),
                eq("c"),
                eq("d")
            )
        verify(mappedVariablesBasedOnAccount, times(1))
            .configRootHandler(
                eq(true),
                eq("a"),
                eq("b"),
                eq("c"),
                eq("d")
            )
        verify(logger, times(1)).info("waiting all...")
    }

    @Test
    fun testAsyncSearch() = runTest {
        val id = "1234"

        whenever(rootFinderClient.asyncHandler(eq(id), any())).thenReturn(
            CompletableDeferred(
                mapOf(
                    "a" to 1,
                    "b" to 2
                )
            )
        )
        whenever(rootFindAccount.asyncHandler(eq(id), any())).thenReturn(
            CompletableDeferred(
                mapOf(
                    "c" to 3,
                    "d" to 4
                )
            )
        )
        assertDoesNotThrow { searchAllVariables.asyncSearch("1234", "a", "b", "c", "d") }
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
            .configRootHandler(
                eq("a"),
                eq("b"),
                eq("c"),
                eq("d")
            )
        verify(mappedVariablesBasedOnAccount, times(1))
            .configRootHandler(
                eq("a"),
                eq("b"),
                eq("c"),
                eq("d")
            )
        verify(logger, times(1)).info("waiting all...")
    }

    @Test
    fun testAsyncSearchWithError() = runTest {
        val id = "1234"

        whenever(rootFinderClient.asyncHandler(eq(id), any())).thenReturn(
            CompletableDeferred(
                mapOf(
                    "a" to 1,
                    "b" to 2
                )
            )
        )

        val deferred = CompletableDeferred<Map<String, *>>()
        deferred.completeExceptionally(RuntimeException("Test"))

        whenever(rootFindAccount.asyncHandler(eq(id), any())).thenReturn(deferred)

        assertThrows<RuntimeException> { searchAllVariables.asyncSearch("1234", "a", "b", "c", "d") }
            .also { assertEquals("Test", it.message) }


        verify(mappedVariablesBasedOnClient, times(1))
            .configRootHandler(
                eq("a"),
                eq("b"),
                eq("c"),
                eq("d")
            )
        verify(mappedVariablesBasedOnAccount, times(1))
            .configRootHandler(
                eq("a"),
                eq("b"),
                eq("c"),
                eq("d")
            )
        verify(logger, times(1)).info("waiting all...")
    }
}