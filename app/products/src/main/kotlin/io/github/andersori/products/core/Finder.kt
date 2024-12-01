package io.github.andersori.products.core

import io.github.andersori.products.core.exceptions.MethodNotImplemented
import java.util.stream.Collectors

abstract class Finder<Identifier, Result>(key: String = "") : Handler<Identifier, Result> {
    private val key: String = key.uppercase()
    private val nextHandlers: MutableList<Handler<Result, *>> = mutableListOf()

    override fun addNext(handler: Handler<Result, *>): Finder<Identifier, Result> {
        nextHandlers.add(handler)
        return this
    }

    override fun handler(identifier: Identifier): Map<String, *> {
        return try {
            val result = execute(identifier)
            return if (nextHandlers.isNotEmpty()) {
                nextHandlers
                    .parallelStream()
                    .map { handler -> handler.handler(result) }
                    .collect(Collectors.toList())
                    .flatMap { it.entries }
                    .associate { it.toPair() }
            } else {
                mapOf(key() to result)
            }
        } catch (ex: RuntimeException) {
            emptyMap<String, Any>()
        }
    }

    override fun key(): String = key

    override fun execute(identifier: Identifier): Result = throw MethodNotImplemented(this.javaClass.name)
}