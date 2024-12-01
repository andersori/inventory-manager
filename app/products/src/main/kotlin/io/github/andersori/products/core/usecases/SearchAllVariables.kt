package io.github.andersori.products.core.usecases

import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class SearchAllVariables(
    private val mappedVariablesBasedOnClient: MappedVariablesBasedOnClient,
    private val mappedVariablesBasedOnAccount: MappedVariablesBasedOnAccount
) {
    private val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(SearchAllVariables::class.java)
    }

    fun search(identifier: String, vararg keys: String): Map<String, *> {
        val distinctKeys = keys.distinct().toTypedArray()
        val vars1 = mappedVariablesBasedOnClient.configRootHandler(*distinctKeys).handler(identifier)
        val vars2 = mappedVariablesBasedOnAccount.configRootHandler(*distinctKeys).handler(identifier)
        return vars1 + vars2
    }

    suspend fun asyncSearch(identifier: String, vararg keys: String): Map<String, *> {
        val distinctKeys = keys.distinct().toTypedArray()
        return customDispatcher.use { dispatcher ->
            val vars1 = mappedVariablesBasedOnClient.configRootHandler(*distinctKeys).asyncHandler(identifier, dispatcher)
            val vars2 = mappedVariablesBasedOnAccount.configRootHandler(*distinctKeys).asyncHandler(identifier, dispatcher)
            logger.info("waiting...")
            vars1.await() + vars2.await()
        }
    }
}