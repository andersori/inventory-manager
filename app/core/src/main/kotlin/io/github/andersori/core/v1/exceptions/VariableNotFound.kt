package io.github.andersori.core.v1.exceptions

import io.github.andersori.core.v1.exceptions.base.BaseException

class VariableNotFound(varName: String) : BaseException("var '$varName' not found") {
    override fun type(): String = "variable_not_found"
}