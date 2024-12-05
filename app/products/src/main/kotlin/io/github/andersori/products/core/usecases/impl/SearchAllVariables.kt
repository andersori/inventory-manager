package io.github.andersori.products.core.usecases.impl

import io.github.andersori.products.core.usecases.AsyncSearchAllVariables
import io.github.andersori.products.core.usecases.SyncSearchAllVariables
import io.github.andersori.products.core.usecases.variables.MappedVariables
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

class SearchAllVariables(
    private vararg val mappedVars: MappedVariables<String, *>,
    private val logger: Logger = CustomLoggerFactory.inline(SearchAllVariables::class.java)
) : SyncSearchAllVariables, AsyncSearchAllVariables {
    private val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    override suspend fun asyncSearch(identifier: String, vararg keys: String): Map<String, *> {
        return customDispatcher.use { dispatcher ->
            val distinctKeys = keys.distinct().toTypedArray()
            val varsFound = mappedVars
                .map { it.configRootHandler(*distinctKeys) }
                .map { it.asyncHandler(identifier, dispatcher) }
                .also { logger.info("waiting all...") }
                .map { it.await() }

            if (varsFound.isNotEmpty())
                varsFound.reduce { vars1, vars2 -> vars1 + vars2 }
            else
                emptyMap<String, Any>()
        }
    }

    override fun search(identifier: String, vararg keys: String): Map<String, *> {
        val distinctKeys = keys.distinct().toTypedArray()
        val varsFound = mappedVars
            .map { it.configRootHandler(ignoreUnknownVar = true, *distinctKeys) }
            .map { it.handler(identifier = identifier, ignoreError = true) }
            .also { logger.info("waiting all...") }

        return if (varsFound.isNotEmpty())
            varsFound.reduce { vars1, vars2 -> vars1 + vars2 }
        else
            emptyMap<String, Any>()
    }
}