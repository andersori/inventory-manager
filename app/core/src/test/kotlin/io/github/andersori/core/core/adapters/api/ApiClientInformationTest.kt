package io.github.andersori.core.core.adapters.api

import io.github.andersori.core.adapters.api.ApiClientInformation
import io.github.andersori.core.exceptions.ClientNotFound
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import kotlin.test.assertEquals

class ApiClientInformationTest {
    private lateinit var httpClient: HttpClient
    private lateinit var apiClientInformation: ApiClientInformation

    @BeforeEach
    fun before() {
        httpClient = mock<HttpClient>()
        apiClientInformation = ApiClientInformation("http://localhost:8080/client/v1", httpClient)
    }

    @Test
    fun `client encontrado`() = runTest {
        val response = mock<HttpResponse<String>>()
        whenever(response.body()).thenReturn("""{"cpf":"000.000.003-00"}""")
        whenever(response.statusCode()).thenReturn(200)
        whenever(
            httpClient.sendAsync(
                any(),
                eq(HttpResponse.BodyHandlers.ofString())
            )
        ).thenReturn(CompletableFuture.completedFuture(response))

        assertDoesNotThrow { apiClientInformation.find("xpto") }
            .also { assertEquals("000.000.003-00", it.cpf) }
    }

    @Test
    fun `falha no envio da requisicao`() = runTest {
        whenever(
            httpClient.sendAsync(
                any(),
                eq(HttpResponse.BodyHandlers.ofString())
            )
        ).thenReturn(CompletableFuture.failedFuture(RuntimeException("Test")))

        assertThrows<ClientNotFound> { apiClientInformation.find("xpto") }
            .also {
                assertEquals("Client not found: api_request/Falha no envio da requisicao para o endpoint `/client/v1/xpto`: Test", it.message)
                assertEquals("client_not_found", it.type())
            }
    }

    @Test
    fun `falha no envio da requisicao, sem descricao do erro`() = runTest {
        whenever(
            httpClient.sendAsync(
                any(),
                eq(HttpResponse.BodyHandlers.ofString())
            )
        ).thenReturn(CompletableFuture.failedFuture(RuntimeException()))

        assertThrows<ClientNotFound> { apiClientInformation.find("xpto") }
            .also {
                assertEquals("Client not found: api_request/Falha no envio da requisicao para o endpoint `/client/v1/xpto`", it.message)
                assertEquals("client_not_found", it.type())
            }
    }

    @Test
    fun `a api retornou o statusCode 500`() = runTest {
        val response = mock<HttpResponse<String>>()
        whenever(response.statusCode()).thenReturn(500)
        whenever(
            httpClient.sendAsync(
                any(),
                eq(HttpResponse.BodyHandlers.ofString())
            )
        ).thenReturn(CompletableFuture.completedFuture(response))

        assertThrows<ClientNotFound> { apiClientInformation.find("xpto") }
            .also {
                assertEquals("Client not found: api_request/A API retornou o statusCode: 500", it.message)
                assertEquals("client_not_found", it.type())
            }
    }

    @Test
    fun `a api retornou o statusCode 199`() = runTest {
        val response = mock<HttpResponse<String>>()
        whenever(response.statusCode()).thenReturn(199)
        whenever(
            httpClient.sendAsync(
                any(),
                eq(HttpResponse.BodyHandlers.ofString())
            )
        ).thenReturn(CompletableFuture.completedFuture(response))

        assertThrows<ClientNotFound> { apiClientInformation.find("xpto") }
            .also {
                assertEquals("Client not found: api_request/A API retornou o statusCode: 199", it.message)
                assertEquals("client_not_found", it.type())
            }
    }
}