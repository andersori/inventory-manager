package io.github.andersori.utils.impl

import io.github.andersori.utils.Logger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class LoggerInlineImplTest {
    private val logger: Logger = LoggerInlineImpl(LoggerInlineImplTest::class.java)

    @Test
    fun testInfo() {
        assertDoesNotThrow { logger.info("AAA") }
    }

    @Test
    fun testError() {
        assertDoesNotThrow { logger.error("AAA") }
    }
}