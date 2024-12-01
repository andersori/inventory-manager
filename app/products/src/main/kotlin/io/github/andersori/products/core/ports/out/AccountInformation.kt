package io.github.andersori.products.core.ports.out

import io.github.andersori.products.core.domain.Account
import io.github.andersori.products.core.exceptions.AccountNotFound

interface AccountInformation {
    @Throws(AccountNotFound::class)
    fun find(id: String): Account
}