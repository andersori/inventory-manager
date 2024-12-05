package io.github.andersori.products.core

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.random.nextLong

interface Handler<Identifier, Result> {
    fun addNext(handler: Handler<Result, *>): Handler<Identifier, Result>
    fun handler(identifier: Identifier, ignoreError: Boolean = true): Map<String, *>
    suspend fun asyncHandler(
        identifier: Identifier,
        dispatcher: CoroutineDispatcher = Dispatchers.Default
    ): Deferred<Map<String, *>> = coroutineScope {
        async(dispatcher) {
            //delay(Random.nextLong(100, 300))
            handler(identifier)
        }
    }
}