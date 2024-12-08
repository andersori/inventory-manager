package io.github.andersori.core.adapters.api

import io.github.andersori.core.adapters.api.dto.ClientDto
import io.github.andersori.core.domain.Client
import io.github.andersori.core.exceptions.ApiRequestException
import io.github.andersori.core.exceptions.ClientNotFound
import io.github.andersori.core.ports.out.ClientInformation
import java.net.http.HttpClient
import java.net.http.HttpHeaders

class ApiClientInformation(
    private val rootUri: String,
    httpClient: HttpClient
) : ApiAsyncRequest<ClientDto>(httpClient), ClientInformation {
    @Throws(ClientNotFound::class)
    override suspend fun find(id: String): Client {
        return try {
            sendAsyncRequest(
                request = buildGetRequest(
                    uri = "$rootUri/$id",
                    headers = mapOf(
                        "Accept" to "application/json"
                    )
                ),
                responseType = ClientDto::class.java
            ).let { Client(cpf = it.cpf) }
        } catch (ex: ApiRequestException) {
            throw ClientNotFound("Client not found: ${ex.type()}/${ex.message}")
        }
    }
}