package io.github.andersori.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class LoggerInlineImpl<T>(clazz: Class<T>) : Logger {
    private val logger: org.slf4j.Logger = LoggerFactory.getLogger(clazz)

    override suspend fun info(msg: String) = coroutineScope {
        launch {
            logger.info(msg)
        }
        Unit
    }
}