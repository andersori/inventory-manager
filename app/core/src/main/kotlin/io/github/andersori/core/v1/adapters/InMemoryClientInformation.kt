package io.github.andersori.core.v1.adapters

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.exceptions.ClientNotFound
import io.github.andersori.core.v1.ports.out.ClientInformation

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