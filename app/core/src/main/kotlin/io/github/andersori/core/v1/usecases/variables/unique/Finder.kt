package io.github.andersori.core.v1.usecases.variables.unique

import io.github.andersori.core.v1.Command
import io.github.andersori.core.v1.Handler
import io.github.andersori.core.v1.exceptions.base.BaseException
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

abstract class Finder<Identifier, Result>(
    key: String,
    val logger: Logger = CustomLoggerFactory.inline(Finder::class.java)
) : Handler<Identifier, Result>, Command<Identifier, Result> {

    private val key: String = key.uppercase()
    private val nextHandlers: MutableList<Handler<Result, *>> = mutableListOf()

    override fun addNext(handler: Handler<Result, *>): Finder<Identifier, Result> {
        nextHandlers.add(handler)
        return this
    }

    @Throws(BaseException::class)
    override suspend fun handler(
        identifier: Identifier,
        ignoreError: Boolean,
        dispatcher: CoroutineDispatcher
    ): Map<String, *> = coroutineScope {
        val result = try {
            execute(identifier)
        } catch (ex: BaseException) {
            logger.error("${ex.javaClass.name} - ${ex.message}")
            if (!ignoreError) {
                throw ex
            } else {
                null
            }
        }

        if (result != null) {
            if (nextHandlers.isNotEmpty()) {
                nextHandlers
                    .map { handler ->
                        async(dispatcher) {
                            //delay(Random.nextLong(100, 300))
                            handler.handler(result, ignoreError, dispatcher)
                        }
                    }
                    .awaitAll()
                    .flatMap { it.entries }
                    .associate { it.toPair() }
            } else {
                mapOf(key() to result)
            }
        } else {
            emptyMap<String, Any>()
        }
    }

    fun key(): String = key
}