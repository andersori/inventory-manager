package io.github.andersori.core.adapters

import io.github.andersori.core.domain.Account
import io.github.andersori.core.exceptions.AccountNotFound
import io.github.andersori.core.ports.out.AccountInformation
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
}