package io.github.andersori.core.exceptions.base

abstract class BaseException(override val message: String) : RuntimeException(message), ErrorType