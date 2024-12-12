package io.github.andersori.core.v2

interface Command<Identifier, Result>  {
    suspend fun execute(identifier: Identifier, ignoreError: Boolean): Result
}

interface CommandWithToken<Identifier, Result> {
    suspend fun execute(token: String, identifier: Identifier, ignoreError: Boolean): Result
}