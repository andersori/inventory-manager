package io.github.andersori.core.v1.adapters.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.andersori.core.v1.exceptions.ApiRequestException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

abstract class ApiAsyncRequest<T>(
    private val httpClient: HttpClient
) {
    private val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    companion object {
        fun is2xx(status: Int): Boolean = status in 200..299
    }

    fun buildGetRequest(uri: String, headers: Map<String, String>): HttpRequest {
        return HttpRequest
            .newBuilder()
            .uri(URI.create(uri))
            .GET()
            .also { requestBuilder -> headers.forEach { header -> requestBuilder.header(header.key, header.value) } }
            .build()
    }

    suspend fun sendAsyncRequest(request: HttpRequest, responseType: Class<T>): T = coroutineScope {
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .exceptionally { ex ->
                val errorMessage = "Falha no envio da requisicao para o endpoint `${request.uri().path}`"
                if (ex.message != null) {
                    throw ApiRequestException("$errorMessage: ${ex.message}")
                }
                throw ApiRequestException(errorMessage)
            }
            .await()
            .let { response ->
                if (is2xx(response.statusCode())) {
                    mapper.readValue(response.body(), responseType)
                } else {
                    throw ApiRequestException("A API retornou o statusCode: ${response.statusCode()}")
                }
            }
    }
}