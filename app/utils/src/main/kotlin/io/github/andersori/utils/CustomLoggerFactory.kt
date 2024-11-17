package io.github.andersori.utils

class CustomLoggerFactory {
    companion object {
        fun <T> inline(clazz: Class<T>) = LoggerInlineImpl(clazz)
    }
}