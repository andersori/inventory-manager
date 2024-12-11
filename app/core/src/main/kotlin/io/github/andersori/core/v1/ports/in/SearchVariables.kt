package io.github.andersori.core.v1.ports.`in`

interface SearchVariables {
    suspend fun search(identifier: String, vararg keys: String): Map<String, *>
}