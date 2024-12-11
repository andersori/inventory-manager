package io.github.andersori.core.v1.exceptions.base

abstract class BaseException(override val message: String) : RuntimeException(message), ErrorType