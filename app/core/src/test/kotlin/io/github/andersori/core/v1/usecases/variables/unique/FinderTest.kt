package io.github.andersori.core.v1.usecases.variables.unique

import io.github.andersori.core.v1.exceptions.base.BaseException
import io.github.andersori.utils.Logger
import kotlinx.coroutines.Dispatchers
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

class FinderTest {
    private lateinit var logger: Logger

    @BeforeEach
    fun before() {
        logger = mock<Logger>()
    }

    @Test
    fun testKey() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test") {
            override suspend fun execute(identifier: String): String = ""
        }
        assertEquals("TEST", finder.key())
    }

    @Test
    fun testAddNext() {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test") {
            override suspend fun execute(identifier: String): String = ""
        }

        assertDoesNotThrow { finder.addNext(finder) }
            .also { assertEquals(finder, it) }
    }

    @Test
    fun testHandler() = runTest {
        val key = "test"
        val response = "..."
        val finder: Finder<String, String> = object : Finder<String, String>(key = key, logger = logger) {
            override suspend fun execute(identifier: String): String = response
        }

        assertDoesNotThrow { finder.handler(identifier = "1234", ignoreError = true, dispatcher = Dispatchers.Default) }
            .also { assertEquals(mapOf(key.uppercase() to response), it) }

        verifyNoInteractions(logger)
    }

    @Test
    fun testHandlerWithOtherHandlers() = runTest {
        val response = "..."
        val finder1: Finder<String, String> = object : Finder<String, String>(key = "test1", logger = logger) {
            override suspend fun execute(identifier: String): String = response
        }
        val finder2: Finder<String, String> = object : Finder<String, String>(key = "test2", logger = logger) {
            override suspend fun execute(identifier: String): String = response
        }

        finder1.addNext(finder2)

        assertDoesNotThrow {
            finder1.handler(
                identifier = "1234",
                ignoreError = true,
                dispatcher = Dispatchers.Default
            )
        }.also { assertEquals(mapOf("TEST2" to response), it) }

        verifyNoInteractions(logger)
    }

    @Test
    fun testHandlerWithThrowErrorOnNextHandler() = runTest {
        val response = "..."
        val finder1: Finder<String, String> = object : Finder<String, String>(key = "test1", logger = logger) {
            override suspend fun execute(identifier: String): String = response
        }
        val finder2: Finder<String, String> = object : Finder<String, String>(key = "test2", logger = logger) {
            override suspend fun execute(identifier: String): String = throw object : BaseException("Testing") {
                override fun type(): String = "test"
            }
        }

        finder1.addNext(finder2)

        val ex = assertThrows<BaseException> {
            finder1.handler(
                identifier = "1234",
                ignoreError = false,
                dispatcher = Dispatchers.Default
            )
        }
        assertEquals("Testing", ex.message)
        assertEquals("test", ex.type())

        verify(
            logger,
            times(1)
        ).error("io.github.andersori.core.v1.usecases.variables.unique.FinderTest\$testHandlerWithThrowErrorOnNextHandler\$1\$finder2\$1\$execute\$2 - Testing")
    }

    @Test
    fun testHandlerWithoutOtherHandlersNotIgnoringEx() = runTest {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test", logger = logger) {
            override suspend fun execute(identifier: String): String = throw object : BaseException("Testing") {
                override fun type(): String = "test"
            }
        }

        val ex = assertThrows<BaseException> {
            finder.handler(
                identifier = "1234",
                ignoreError = false,
                dispatcher = Dispatchers.Default
            )
        }
        assertEquals("Testing", ex.message)
        assertEquals("test", ex.type())

        verify(
            logger,
            times(1)
        ).error("io.github.andersori.core.v1.usecases.variables.unique.FinderTest\$testHandlerWithoutOtherHandlersNotIgnoringEx\$1\$finder\$1\$execute\$2 - Testing")
    }

    @Test
    fun testHandlerWithoutOtherHandlersIgnoringEx() = runTest {
        val finder: Finder<String, String> = object : Finder<String, String>(key = "test", logger = logger) {
            override suspend fun execute(identifier: String): String = throw object : BaseException("Testing") {
                override fun type(): String = "test"
            }
        }

        assertDoesNotThrow {
            finder.handler(
                identifier = "1234",
                ignoreError = true,
                dispatcher = Dispatchers.Default
            )
        }.also { assertEquals(emptyMap<String, String>(), it) }

        verify(
            logger,
            times(1)
        ).error("io.github.andersori.core.v1.usecases.variables.unique.FinderTest\$testHandlerWithoutOtherHandlersIgnoringEx\$1\$finder\$1\$execute\$2 - Testing")
    }
}