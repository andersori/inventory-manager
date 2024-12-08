package io.github.andersori.core.adapters

import io.github.andersori.core.domain.Client
import io.github.andersori.core.exceptions.ClientNotFound
import io.github.andersori.core.ports.out.ClientInformation

class InMemoryClientInformation : ClientInformation {
    @Throws(ClientNotFound::class)
    override suspend fun find(id: String): Client {
        if (id.equals("TEST", true)) {
            throw ClientNotFound("Test Client not found")
        }
        return Client(
            cpf = "000.000.003-00"
        )
    }
}