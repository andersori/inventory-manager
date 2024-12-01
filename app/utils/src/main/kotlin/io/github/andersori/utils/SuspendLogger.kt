package io.github.andersori.utils

interface SuspendLogger {
    suspend fun info(msg: String)
}