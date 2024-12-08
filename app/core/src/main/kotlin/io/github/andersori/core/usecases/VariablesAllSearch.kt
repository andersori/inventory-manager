package io.github.andersori.core.usecases

import io.github.andersori.core.ports.`in`.SearchVariables
import io.github.andersori.core.usecases.variables.MappedVariables
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class VariablesAllSearch(
    private vararg val mappedVars: MappedVariables<String, *>,
    private val logger: Logger = CustomLoggerFactory.inline(VariablesAllSearch::class.java)
) : SearchVariables {
    override suspend fun search(identifier: String, vararg keys: String): Map<String, *> = coroutineScope {
        val distinctKeys = keys.distinct().toTypedArray()

        val varsFound = mappedVars
            .mapNotNull { it.configRootHandler(*distinctKeys) }
            .map {
                async {
                    it.handler(
                        identifier = identifier,
                        ignoreError = true,
                        dispatcher = Dispatchers.Default
                    )
                }
            }
            .awaitAll()
            .also { logger.info("waiting all...") }

        if (varsFound.isNotEmpty())
            varsFound.reduce { vars1, vars2 -> vars1 + vars2 }
        else
            emptyMap<String, Any>()
    }

}