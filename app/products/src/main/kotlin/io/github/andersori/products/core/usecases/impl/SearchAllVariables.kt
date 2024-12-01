package io.github.andersori.products.core.usecases.impl

import io.github.andersori.products.core.usecases.AsyncSearchAllVariables
import io.github.andersori.products.core.usecases.SyncSearchAllVariables
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

class SearchAllVariables(
    private val mappedVariablesBasedOnClient: MappedVariablesBasedOnClient,
    private val mappedVariablesBasedOnAccount: MappedVariablesBasedOnAccount
) : SyncSearchAllVariables, AsyncSearchAllVariables {
    private val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    companion object {
        private val logger: Logger = CustomLoggerFactory.inline(SearchAllVariables::class.java)
    }

    override suspend fun asyncSearch(identifier: String, vararg keys: String): Map<String, *> {
        val distinctKeys = keys.distinct().toTypedArray()
        return customDispatcher.use { dispatcher ->
            val vars1 =
                mappedVariablesBasedOnClient.configRootHandler(*distinctKeys).asyncHandler(identifier, dispatcher)
            val vars2 =
                mappedVariablesBasedOnAccount.configRootHandler(*distinctKeys).asyncHandler(identifier, dispatcher)
            logger.info("waiting...")
            vars1.await() + vars2.await()
        }
    }

    override fun syncSearch(identifier: String, vararg keys: String): Map<String, *> {
        val distinctKeys = keys.distinct().toTypedArray()
        val vars1 = mappedVariablesBasedOnClient.configRootHandler(*distinctKeys)
            .handler(identifier = identifier, ignoreError = true)
        val vars2 = mappedVariablesBasedOnAccount.configRootHandler(*distinctKeys)
            .handler(identifier = identifier, ignoreError = false)
        return vars1 + vars2
    }
}