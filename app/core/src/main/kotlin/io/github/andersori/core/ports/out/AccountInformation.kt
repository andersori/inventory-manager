package io.github.andersori.core.ports.out

import io.github.andersori.core.domain.Account
import io.github.andersori.core.exceptions.AccountNotFound

interface AccountInformation {
    @Throws(AccountNotFound::class)
    suspend fun find(id: String): Account
}