package io.github.andersori.core.core

import io.github.andersori.core.Handler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

class HandlerTest {
    private val handler: Handler<String, Int> = object : Handler<String, Int> {
        override fun addNext(handler: Handler<Int, *>): Handler<String, Int> {
            return this
        }

        override suspend fun handler(
            identifier: String,
            ignoreError: Boolean,
            dispatcher: CoroutineDispatcher
        ): Map<String, *> {
            return emptyMap<String, Any>()
        }

    }

    @Test
    fun testAddNext() {
        assertDoesNotThrow { handler.addNext(mock<Handler<Int, Int>>()) }
            .also { assertEquals(handler, it) }
    }

    @Test
    fun testHandler1() = runTest {
        assertDoesNotThrow { handler.handler("xpto", true, Dispatchers.Default) }
            .also { assertEquals(emptyMap<String, Int>(), it) }
    }

    @Test
    fun testHandler2() = runTest {
        assertDoesNotThrow { handler.handler(identifier = "xpto", dispatcher = Dispatchers.Default) }
            .also { assertEquals(emptyMap<String, Int>(), it) }
    }
}