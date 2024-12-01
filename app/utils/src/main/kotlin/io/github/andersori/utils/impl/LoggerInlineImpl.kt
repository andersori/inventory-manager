package io.github.andersori.utils.impl

import io.github.andersori.utils.Logger
import org.slf4j.LoggerFactory

class LoggerInlineImpl<T>(clazz: Class<T>) : Logger {
    private val logger: org.slf4j.Logger = LoggerFactory.getLogger(clazz)

    override fun info(msg: String) = logger.info(msg)
    override fun error(msg: String) = logger.error(msg)
}