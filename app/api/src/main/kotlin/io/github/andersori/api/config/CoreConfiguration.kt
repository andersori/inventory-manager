package io.github.andersori.api.config

import io.github.andersori.core.v1.adapters.InMemoryAccountInformation
import io.github.andersori.core.v1.adapters.api.ApiClientInformation
import io.github.andersori.core.v1.domain.Account
import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.ports.`in`.SearchVariables
import io.github.andersori.core.v1.usecases.VariablesAllSearch
import io.github.andersori.core.v1.usecases.variables.MappedVariables
import io.github.andersori.core.v1.usecases.variables.MappedVariablesBasedOnAccount
import io.github.andersori.core.v1.usecases.variables.MappedVariablesBasedOnClient
import io.github.andersori.core.v1.usecases.variables.unique.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient

@Configuration
class CoreConfiguration(
    private val httpClient: HttpClient
) {
    @Bean
    fun searchVariables(): SearchVariables {
        val mappedVariablesBasedOnClient: MappedVariables<String, Client> =
            MappedVariablesBasedOnClient(
                clientInformation = ApiClientInformation(
                    httpClient = httpClient,
                    rootUri = "http://localhost:8080/client/v1"
                ),
                FindSPG(),
                FindStates()
            )
        val mappedVariablesBasedOnAccount: MappedVariables<String, Account> =
            MappedVariablesBasedOnAccount(
                accountInformation = InMemoryAccountInformation(),
                FindTestAccount(),
                FindActiveAccount(),
                FindFakeAccount()
            )

        return VariablesAllSearch(
            mappedVariablesBasedOnClient,
            mappedVariablesBasedOnAccount
        )
    }
}