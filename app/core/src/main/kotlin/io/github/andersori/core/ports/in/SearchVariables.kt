package io.github.andersori.core.ports.`in`

interface SearchVariables {
    suspend fun search(identifier: String, vararg keys: String): Map<String, *>
}