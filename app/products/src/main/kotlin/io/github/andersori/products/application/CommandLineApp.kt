package io.github.andersori.products.application

import io.github.andersori.products.core.usecases.SearchAllVariables
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

fun main(vararg args: String) {
    CommandLineApp.logger.info("iniciando CommandLineApp")

    val searchAllVariables = SearchAllVariables(
        mappedVariablesBasedOnClient = MappedVariablesBasedOnClient(),
        mappedVariablesBasedOnAccount = MappedVariablesBasedOnAccount()
    )

    val customArgs = arrayOf(
        "spg",
        "spg",
        "spg",
        "spg",
        "spg",
        "states",
        "test_user",
        "active_user"
    )

    println("Run with -> ${customArgs.contentToString()}")
    println("-----------------------------------------------")
    println(
        "Sync Result -> ${
            searchAllVariables.search(
                "1234",
                *customArgs
            )
        }"
    )
    println("-----------------------------------------------")
    runBlocking {
        println(
            "Sync Result -> ${
                searchAllVariables.asyncSearch(
                    "1234",
                    *customArgs
                )
            }"
        )
    }
}