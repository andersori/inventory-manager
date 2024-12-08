package io.github.andersori.core.usecases.variables

import io.github.andersori.core.exceptions.VariableNotFound
import io.github.andersori.core.usecases.variables.unique.Finder

abstract class MappedVariables<Identifier, Result>(private val mappedVars: Map<String, Finder<Result, *>>) {

    fun configRootHandler(vararg vars: String): Finder<Identifier, Result>? {
        return configRootHandler(
            ignoreUnknownVar = true,
            vars = vars
        )
    }

    fun configRootHandler(ignoreUnknownVar: Boolean, vararg vars: String): Finder<Identifier, Result>? {
        val root = getNewRoot()

        val foundKeys = vars.associate { key ->
            addHandlers(mappedVars[key.uppercase()], key, ignoreUnknownVar, root)
        }.filterValues { it != null }

        if (foundKeys.isNotEmpty()) {
            return root
        }
        return null
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
                throw VariableNotFound(key)
            }
        }
    }
}