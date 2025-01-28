package com.mooncloak.kodetools.kmd

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CommandGroupTest {

    @Test
    fun `verify CommandGroupResult properties`() {
        val results = listOf(
            CommandResult("command1", listOf("arg1"), ExitCode.DefaultSuccess),
            CommandResult("command2", listOf("arg2"), ExitCode.DefaultError)
        )

        val commandGroupResult = CommandGroupResult(results)

        assertEquals(results, commandGroupResult.results)
    }

    @Test
    fun `verify CommandGroupResult equality and hashCode`() {
        val results = listOf(
            CommandResult("command1", listOf("arg1"), ExitCode.DefaultSuccess),
            CommandResult("command2", listOf("arg2"), ExitCode.DefaultError)
        )

        val sameOne = CommandGroupResult(results)
        val sameTwo = CommandGroupResult(results)
        val diffOne = CommandGroupResult(results + CommandResult("command3", listOf("arg3"), ExitCode.DefaultSuccess))

        assertEquals(sameOne, sameTwo)
        assertEquals(sameOne.hashCode(), sameTwo.hashCode())

        assertNotEquals(sameOne, diffOne)
        assertNotEquals(sameOne.hashCode(), diffOne.hashCode())

        assertNotEquals(sameTwo, diffOne)
        assertNotEquals(sameTwo.hashCode(), diffOne.hashCode())
    }
}
