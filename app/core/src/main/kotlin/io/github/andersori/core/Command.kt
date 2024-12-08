package io.github.andersori.core

interface Command<Identifier, Result> {
    suspend fun execute(identifier: Identifier): Result
}