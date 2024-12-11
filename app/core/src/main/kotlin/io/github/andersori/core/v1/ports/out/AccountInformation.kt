package io.github.andersori.core.v1.ports.out

import io.github.andersori.core.v1.domain.Account
import io.github.andersori.core.v1.exceptions.AccountNotFound

interface AccountInformation {
    @Throws(AccountNotFound::class)
    suspend fun find(id: String): Account
}