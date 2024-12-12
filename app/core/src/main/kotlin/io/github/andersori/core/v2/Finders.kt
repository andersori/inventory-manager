package io.github.andersori.core.v2

import io.github.andersori.core.v1.domain.Client
import io.github.andersori.core.v1.exceptions.AccountNotFound
import io.github.andersori.core.v1.exceptions.TokenException
import io.github.andersori.core.v1.ports.out.ClientInformation
import io.github.andersori.core.v1.ports.out.TokenInformation
import kotlinx.coroutines.runBlocking

abstract class RootFinder(val next: List<RootFinderWithToken<*>> = emptyList()) : Command<String, Map<String, Any>>

abstract class RootFinderWithToken<Identifier>(val next: List<FinderWithKey<Identifier, *>> = emptyList()) :
    CommandWithToken<String, Map<String, Any>> {
    fun configure(varsToFound: List<String>) {
        next.forEach {
            if (varsToFound.contains(it.key)) {
                it.isActive = true
            }
        }
    }
}

abstract class FinderWithKey<Identifier, Result>(val key: String, var isActive: Boolean = false) :
    Command<Identifier, Result>

class SPGFinder : FinderWithKey<Client, String>("SPG") {
    override suspend fun execute(identifier: Client, ignoreError: Boolean): String {
        return "00"
    }
}

class MPDFinder : FinderWithKey<Client, String>("MPD") {
    override suspend fun execute(identifier: Client, ignoreError: Boolean): String {
        return "11"
    }
}

class ClientFinder(
    private val clientInformation: ClientInformation,
    next: List<FinderWithKey<Client, *>>
) : RootFinderWithToken<Client>(next) {
    override suspend fun execute(token: String, identifier: String, ignoreError: Boolean): Map<String, Any> {
        val client: Client? = try {
            clientInformation.find(token, identifier)
        } catch (ex: AccountNotFound) {
            if (!ignoreError) {
                throw ex
            }
            null
        }

        if (client != null && next.isNotEmpty()) {
            return next.associate { command -> command.key to command.execute(client, ignoreError) }
                .filterValues { it != null }
                .map { it.key to it.value as Any }
                .toMap()
        }
        return emptyMap()
    }

}


class TokenRootFinder(
    private val tokenInformation: TokenInformation,
    next: List<RootFinderWithToken<*>>
) : RootFinder(next) {
    override suspend fun execute(identifier: String, ignoreError: Boolean): Map<String, Any> {
        val token: String? = try {
            tokenInformation.auth()
        } catch (ex: TokenException) {
            if (!ignoreError) {
                throw ex
            }
            null
        }

        if (token != null && next.isNotEmpty()) {
            return next.map { command -> command.execute(token, identifier, ignoreError) }
                .reduce { map1, map2 -> map1 + map2 }
        }
        return emptyMap()
    }

}

fun main() {
    val rdgFinder = SPGFinder()
    val clientFinder = ClientFinder(object : ClientInformation {
        override suspend fun find(id: String): Client = Client(
            cpf = "00000000000"
        )

        override suspend fun find(token: String, id: String): Client = Client(
            cpf = "00000000000"
        )
    }, listOf(rdgFinder))

    val tokenRootFinder = TokenRootFinder(object : TokenInformation {
        override suspend fun auth(): String = "..."
    }, listOf(clientFinder))

    runBlocking {
        println(tokenRootFinder.execute("xpto", true))
    }
}