package io.github.andersori.utils

import io.github.andersori.utils.impl.LoggerInlineImpl

sealed class CustomLoggerFactory {
    companion object {
        fun <T> inline(clazz: Class<T>): Logger = LoggerInlineImpl(clazz)
    }
}