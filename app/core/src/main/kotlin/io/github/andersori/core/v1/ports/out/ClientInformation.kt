package io.github.andersori.core.v1.ports.out

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.exceptions.ClientNotFound

interface ClientInformation {
    @Throws(ClientNotFound::class)
    suspend fun find(id: String): Client

    @Throws(ClientNotFound::class)
    suspend fun find(token: String, id: String): Client
}