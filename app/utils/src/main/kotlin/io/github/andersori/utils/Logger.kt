package io.github.andersori.utils

interface Logger {
    suspend fun info(msg: String)
}