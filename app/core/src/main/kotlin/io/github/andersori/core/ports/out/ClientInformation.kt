package io.github.andersori.core.ports.out

import io.github.andersori.core.domain.Client
import io.github.andersori.core.exceptions.ClientNotFound

interface ClientInformation {
    @Throws(ClientNotFound::class)
    suspend fun find(id: String): Client
}