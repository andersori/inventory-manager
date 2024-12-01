package io.github.andersori.products.application

import io.github.andersori.products.core.usecases.AsyncSearchAllVariables
import io.github.andersori.products.core.usecases.SyncSearchAllVariables
import io.github.andersori.products.core.usecases.impl.SearchAllVariables
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.runBlocking

class CommandLineApp {
    companion object {
        val logger: Logger = CustomLoggerFactory.inline(CommandLineApp::class.java)
    }
}

fun main() {
    CommandLineApp.logger.info("iniciando CommandLineApp")

    val searchImplementation = SearchAllVariables(
        mappedVariablesBasedOnClient = MappedVariablesBasedOnClient(),
        mappedVariablesBasedOnAccount = MappedVariablesBasedOnAccount()
    )
    val syncSearchAllVariables: SyncSearchAllVariables = searchImplementation
    val asyncSearchAllVariables: AsyncSearchAllVariables = searchImplementation

    val customArgs = arrayOf(
        "spg",
        "spg",
        "spg",
        "spg",
        "spg",
        "states",
        "test_account",
        "active_account"
    )

    println("Run with -> ${customArgs.contentToString()}")
    println("-----------------------------------------------")
    println(
        "Sync Result -> ${
            syncSearchAllVariables.syncSearch(
                "1234",
                *customArgs
            )
        }"
    )
    println("-----------------------------------------------")
    runBlocking {
        println(
            "Sync Result -> ${
                asyncSearchAllVariables.asyncSearch(
                    "1234",
                    *customArgs
                )
            }"
        )
    }
}