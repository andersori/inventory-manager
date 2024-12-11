package io.github.andersori.core.v1

interface Command<Identifier, Result> {
    suspend fun execute(identifier: Identifier): Result
}