package io.github.andersori.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.mock

class LoggerTest {
    private val logger: Logger = mock<Logger>()

    @Test
    fun testInfo() {
        assertDoesNotThrow { logger.info("AAA") }
    }

    @Test
    fun testError() {
        assertDoesNotThrow { logger.error("AAA") }
    }
}