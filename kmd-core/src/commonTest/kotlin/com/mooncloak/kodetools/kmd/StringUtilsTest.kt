package com.mooncloak.kodetools.kmd

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest {

    @Test
    fun `splitOnWhiteSpace does not breakup strings without spaces`() {
        val result = "abc".splitOnWhiteSpace()
        assertEquals(listOf("abc"), result)
    }

    @Test
    fun `splitOnWhiteSpace handles single spaces`() {
        val result = "a b c".splitOnWhiteSpace()
        assertEquals(listOf("a", "b", "c"), result)
    }

    @Test
    fun `splitOnWhiteSpace handles multiple spaces`() {
        val result = "a   b   c".splitOnWhiteSpace()
        assertEquals(listOf("a", "b", "c"), result)
    }

    @Test
    fun `splitOnWhiteSpace trims leading and trailing spaces`() {
        val result = "   a b c   ".splitOnWhiteSpace()
        assertEquals(listOf("a", "b", "c"), result)
    }

    @Test
    fun `splitOnWhiteSpace trims leading and trailing spaces with single word`() {
        val result = "   abc   ".splitOnWhiteSpace()
        assertEquals(listOf("abc"), result)
    }

    @Test
    fun `splitOnWhiteSpace returns empty list for empty string`() {
        val result = "".splitOnWhiteSpace()
        assertEquals(emptyList(), result)
    }

    @Test
    fun `splitOnWhiteSpace handles strings with only spaces`() {
        val result = "     ".splitOnWhiteSpace()
        assertEquals(emptyList(), result)
    }
}
