package io.github.andersori.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient

@Configuration
class HttpClientConfig {
    @Bean
    fun httpClient() = HttpClient.newBuilder().build()
}