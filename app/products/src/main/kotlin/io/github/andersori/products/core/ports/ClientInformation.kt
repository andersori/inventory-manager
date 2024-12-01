package io.github.andersori.products.core.ports

import io.github.andersori.products.core.domain.Client
import io.github.andersori.products.core.exceptions.ClientNotFound

interface ClientInformation {
    @Throws(ClientNotFound::class)
    fun find(id: String): Client
}