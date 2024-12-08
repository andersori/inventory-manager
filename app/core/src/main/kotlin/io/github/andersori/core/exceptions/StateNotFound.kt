package io.github.andersori.core.exceptions

import io.github.andersori.core.exceptions.base.BaseException

class StateNotFound(message: String) : BaseException(message) {
    override fun type(): String = "enum_states"
}