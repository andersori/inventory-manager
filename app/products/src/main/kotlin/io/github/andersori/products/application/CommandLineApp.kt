package io.github.andersori.products.application

import io.github.andersori.products.core.usecases.AsyncSearchAllVariables
import io.github.andersori.products.core.usecases.SyncSearchAllVariables
import io.github.andersori.products.core.usecases.impl.SearchAllVariables
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.products.core.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.utils.CustomLoggerFactory
import io.github.andersori.utils.Logger
import kotlinx.coroutines.runBlocking
import kotlin.system.measureNanoTime

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
        "states",
        "test_account",
        "active_account",
        "fake_account"
    )

    println("Run with -> ${customArgs.contentToString()}")
    println("-----------------------------------------------")
    val elapsedTime1 = measureNanoTime {
        try {
            println(
                "Sync Result -> ${
                    syncSearchAllVariables.syncSearch(
                        identifier = "1234",
                        *customArgs
                    )
                }"
            )
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }
    println("Tempo de execução: ${elapsedTime1 / 1_000_000}ms")

    println("-----------------------------------------------")
    val elapsedTime2 = measureNanoTime {
        try {
            runBlocking {
                println(
                    "Sync Result -> ${
                        asyncSearchAllVariables.asyncSearch(
                            identifier = "1234",
                            *customArgs
                        )
                    }"
                )
            }
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
        }
    }
    println("Tempo de execução: ${elapsedTime2 / 1_000_000}ms")
}