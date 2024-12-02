package io.github.andersori.products.core

import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import java.util.stream.Collectors

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

    override fun handler(identifier: Identifier, ignoreError: Boolean): Map<String, *> {
        val result = try {
            execute(identifier)
        } catch (ex: RuntimeException) {
            logger.error(ex.message ?: "erro desconhecido")
            if (!ignoreError) {
                throw ex
            } else {
                null
            }
        }
        return if (result != null) {
            return if (nextHandlers.isNotEmpty()) {
                nextHandlers
                    .parallelStream()
                    .map { handler -> handler.handler(result, ignoreError) }
                    .collect(Collectors.toList())
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