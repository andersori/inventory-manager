package io.github.andersori.core.v1.adapters

import io.github.andersori.core.v1.domain.Account
import io.github.andersori.core.v1.exceptions.AccountNotFound
import io.github.andersori.core.v1.ports.out.AccountInformation
import java.util.*

class InMemoryAccountInformation : AccountInformation {
    @Throws(AccountNotFound::class)
    override suspend fun find(id: String): Account {
        if (id.equals("TEST", true)) {
            throw AccountNotFound("Test Account not found")
        }
        return Account(
            id = id,
            name = "TEST",
            active = true
        )
    }

    override suspend fun find(token: String, id: String): Account {
        TODO("Not yet implemented")
    }
}