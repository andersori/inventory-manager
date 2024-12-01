package io.github.andersori.products.core

import kotlinx.coroutines.*

interface Handler<Identifier, Result> : Command<Identifier, Result> {
    fun addNext(handler: Handler<Result, *>): Handler<Identifier, Result>
    fun handler(identifier: Identifier, ignoreError: Boolean = true): Map<String, *>
    suspend fun asyncHandler(
        identifier: Identifier,
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ): Deferred<Map<String, *>> = coroutineScope {
        async(dispatcher) {
            handler(identifier)
        }
    }
}