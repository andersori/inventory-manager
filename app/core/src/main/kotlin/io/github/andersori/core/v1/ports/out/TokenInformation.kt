package io.github.andersori.core.v1.ports.out

import io.github.andersori.core.v1.exceptions.TokenException

interface TokenInformation {
    @Throws(TokenException::class)
    suspend fun auth(): String
}