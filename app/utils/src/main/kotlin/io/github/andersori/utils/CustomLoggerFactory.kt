package io.github.andersori.utils

import io.github.andersori.utils.impl.LoggerInlineImpl
import io.github.andersori.utils.impl.SuspendLoggerInlineImpl

class CustomLoggerFactory {
    companion object {
        fun <T> inline(clazz: Class<T>): Logger = LoggerInlineImpl(clazz)
        fun <T> inlineSuspend(clazz: Class<T>): SuspendLogger = SuspendLoggerInlineImpl(clazz)
    }
}