package io.github.andersori.products.core

interface Command<Identifier, Result> {
    fun execute(identifier: Identifier): Result
}