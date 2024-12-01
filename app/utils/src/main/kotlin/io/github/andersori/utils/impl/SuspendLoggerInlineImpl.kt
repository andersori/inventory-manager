package io.github.andersori.utils.impl

import io.github.andersori.utils.SuspendLogger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

class SuspendLoggerInlineImpl<T>(clazz: Class<T>) : SuspendLogger {
    private val logger: org.slf4j.Logger = LoggerFactory.getLogger(clazz)

    override suspend fun info(msg: String) = coroutineScope {
        launch {
            logger.info(msg)
        }
        Unit
    }
}