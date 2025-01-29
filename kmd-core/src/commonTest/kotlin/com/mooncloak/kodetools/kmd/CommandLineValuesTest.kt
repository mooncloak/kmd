package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalKmdApi::class)
class CommandLineValuesTest {

    @Test
    fun `commandToValues handles CommandLineValues`() = runTest {
        val result = CommandLineValues.invoke("command").get()
        assertEquals(listOf("command"), result)
    }

    @Test
    fun `commandToValues doesn't split on whitespace for CommandLineValues`() = runTest {
        val result = CommandLineValues.invoke("one two").get()
        assertEquals(listOf("one two"), result)
    }

    @Test
    fun `commandToValues handles single word command`() = runTest {
        val result = "command".commandToValues(splitOnWhitespace = true)
        assertEquals(listOf("command"), result)
    }

    @Test
    fun `commandToValues handles space-separated command`() = runTest {
        val result = "command arg1 arg2".commandToValues(splitOnWhitespace = true)
        assertEquals(listOf("command", "arg1", "arg2"), result)
    }

    @Test
    fun `commandToValues handles empty string`() = runTest {
        val result = "".commandToValues(splitOnWhitespace = true)
        assertTrue { result.isEmpty() }
    }

    @Test
    fun `commandToValues handles multiple spaces`() = runTest {
        val result = "command   arg1    arg2".commandToValues(splitOnWhitespace = true)
        assertEquals(listOf("command", "arg1", "arg2"), result)
    }

    @Test
    fun `commandToValues trims leading and trailing spaces`() = runTest {
        val result = "   command arg1 arg2   ".commandToValues(splitOnWhitespace = true)
        assertEquals(listOf("command", "arg1", "arg2"), result)
    }
}
