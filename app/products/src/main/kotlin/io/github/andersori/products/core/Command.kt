package io.github.andersori.products.core

interface Command<Identifier, Result> {
    fun key(): String
    fun execute(identifier: Identifier): Result
}