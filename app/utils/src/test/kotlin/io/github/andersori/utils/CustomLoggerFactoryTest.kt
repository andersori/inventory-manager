package io.github.andersori.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertNotNull

class CustomLoggerFactoryTest {
    @Test
    fun testInline() {
        assertDoesNotThrow { CustomLoggerFactory.inline(CustomLoggerFactoryTest::class.java) }
            .also { assertNotNull(it) }
    }
}