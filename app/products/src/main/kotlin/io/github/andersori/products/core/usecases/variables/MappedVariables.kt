package io.github.andersori.products.core.usecases.variables

import io.github.andersori.products.core.Finder
import io.github.andersori.products.core.exceptions.UnnecessaryExecution
import io.github.andersori.products.core.exceptions.VariableNotFound

abstract class MappedVariables<Identifier, Result>(private val mappedVars: Map<String, Finder<Result, *>>) {

    companion object {
        fun <Identifier, Result> getDefaultFinder(rootKey: String) = object : Finder<Identifier, Result>(rootKey) {
            override fun execute(identifier: Identifier): Result {
                throw UnnecessaryExecution("for key = ${key()}")
            }
        }
    }

    fun configRootHandler(vararg vars: String): Finder<Identifier, Result> {
        return configRootHandler(
            ignoreUnknownVar = true,
            vars = vars
        )
    }

    fun configRootHandler(ignoreUnknownVar: Boolean, vararg vars: String): Finder<Identifier, Result> {
        val root = getNewRoot()

        val foundKeys = vars.associate { key ->
            addHandlers(mappedVars[key.uppercase()], key, ignoreUnknownVar, root)
        }.filterValues { it != null }

        if (foundKeys.isEmpty()) {
            return getDefaultFinder(root.key())
        }

        return root
    }

    abstract fun getNewRoot(): Finder<Identifier, Result>

    private fun addHandlers(
        finder: Finder<Result, *>?,
        key: String,
        ignoreUnknownVar: Boolean,
        root: Finder<Identifier, Result>
    ): Pair<String, Finder<Identifier, Result>?> {
        return if (finder != null) {
            key to root.addNext(finder)
        } else {
            if (ignoreUnknownVar) {
                key to null
            } else {
                throw VariableNotFound("var $key not found")
            }
        }
    }
}