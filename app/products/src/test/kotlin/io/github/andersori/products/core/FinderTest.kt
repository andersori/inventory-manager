package io.github.andersori.products.core

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
import kotlin.test.assertNull

class FinderTest {
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
    }

    @Test
    fun testKey() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test") {
            override fun execute(identifier: String): String {
                return "..."
            }
        }
        assertEquals("TEST", finder.key())
    }

    @Test
    fun testAddNext() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test") {
            override fun execute(identifier: String): String {
                return "..."
            }
        }

        assertDoesNotThrow { finder.addNext(finder) }
            .also { assertEquals(finder, it) }
    }

    @Test
    fun testAsyncHandler() = runTest {
        val key = "test"
        val response = "..."
        val finder: Finder<String, String> = object : Finder<String, String>(key = key, logger = logger) {
            override fun execute(identifier: String): String {
                return response
            }
        }

        assertDoesNotThrow { finder.asyncHandler(identifier = "1234") }
            .await()
            .also {
                assertEquals(
                    mapOf(
                        key.uppercase() to response
                    ), it
                )
            }

        verifyNoInteractions(logger)
    }

    @Test
    fun testHandlerWithoutOtherHandlers() {
        val key = "test"
        val response = "..."
        val finder: Finder<String, String> = object : Finder<String, String>(key = key, logger = logger) {
            override fun execute(identifier: String): String {
                return response
            }
        }

        assertDoesNotThrow { finder.handler(identifier = "1234", ignoreError = true) }
            .also {
                assertEquals(
                    mapOf(
                        key.uppercase() to response
                    ), it
                )
            }

        verifyNoInteractions(logger)
    }

    @Test
    fun testHandlerWithOtherHandlers() {
        val response = "..."
        val finder1: Finder<String, String> = object : Finder<String, String>(key = "test1", logger = logger) {
            override fun execute(identifier: String): String {
                return response
            }
        }
        val finder2: Finder<String, String> = object : Finder<String, String>(key = "test2", logger = logger) {
            override fun execute(identifier: String): String {
                return response
            }
        }

        finder1.addNext(finder2)

        assertDoesNotThrow { finder1.handler(identifier = "1234", ignoreError = true) }
            .also {
                assertEquals(
                    mapOf(
                        "TEST2" to response
                    ), it
                )
            }

        verifyNoInteractions(logger)
    }

    @Test
    fun testHandlerWithoutOtherHandlersThrowEx() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test", logger = logger) {
            override fun execute(identifier: String): String {
                throw RuntimeException("Test")
            }
        }

        val ex = assertThrows<RuntimeException> { finder.handler(identifier = "1234", ignoreError = false) }
        assertEquals("Test", ex.message)

        verify(logger, times(1)).error(ex.message!!)
    }

    @Test
    fun testHandlerWithoutOtherHandlersThrowUnknownEx() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test", logger = logger) {
            override fun execute(identifier: String): String {
                throw RuntimeException()
            }
        }

        assertThrows<RuntimeException> { finder.handler(identifier = "1234", ignoreError = false) }
            .also { assertNull(it.message) }

        verify(logger, times(1)).error("erro desconhecido")
    }

    @Test
    fun testHandlerWithoutOtherHandlersIgnoringUnknownEx() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test", logger = logger) {
            override fun execute(identifier: String): String {
                throw RuntimeException()
            }
        }

        assertDoesNotThrow { finder.handler(identifier = "1234", ignoreError = true) }
            .also { assertEquals(emptyMap<String, String>(), it) }

        verify(logger, times(1)).error("erro desconhecido")
    }
}