package io.github.andersori.core.exceptions

import io.github.andersori.core.exceptions.base.BaseException

class InvalidSPG(message: String) : BaseException(message) {
    override fun type(): String = "invalid_spg"
}