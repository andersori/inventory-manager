package io.github.andersori.core.v1

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

class CommandTest {
    @Test
    fun testExecute() = runTest {
        assertDoesNotThrow {
            object : Command<String, Int> {
                override suspend fun execute(identifier: String): Int = 0
            }.execute("xpto")
        }.also { assertEquals(0, it) }
    }
}