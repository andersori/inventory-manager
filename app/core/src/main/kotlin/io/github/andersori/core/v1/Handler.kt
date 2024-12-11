package io.github.andersori.core.v1

import kotlinx.coroutines.CoroutineDispatcher

interface Handler<Identifier, Result> {
    fun addNext(handler: Handler<Result, *>): Handler<Identifier, Result>
    suspend fun handler(identifier: Identifier, ignoreError: Boolean = true, dispatcher: CoroutineDispatcher): Map<String, *>
}