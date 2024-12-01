package io.github.andersori.products.core

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class FinderFinderTest {
    private val finder: Finder<String, String> = mock<Finder<String, String>> {
        on { execute(any<String>()) } doReturn "00"
    }

    @Test
    fun test() {
        println(finder.execute(identifier = "1234"))
    }
}