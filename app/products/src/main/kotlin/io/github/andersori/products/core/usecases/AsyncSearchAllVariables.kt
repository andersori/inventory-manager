package io.github.andersori.products.core.usecases

interface AsyncSearchAllVariables {
    suspend fun asyncSearch(identifier: String, vararg keys: String): Map<String, *>
}