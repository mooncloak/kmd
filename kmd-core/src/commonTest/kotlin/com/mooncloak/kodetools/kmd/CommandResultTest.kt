package com.mooncloak.kodetools.kmd

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CommandResultTest {

    @Test
    fun `verify CommandResult properties`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val exitCode = ExitCode.DefaultSuccess // Assuming this is defined elsewhere

        val commandResult = CommandResult(command, arguments, exitCode)

        assertEquals(command, commandResult.command)
        assertEquals(arguments, commandResult.arguments)
        assertEquals(exitCode, commandResult.exitCode)
    }

    @Test
    fun `verify CommandResult equality and hashCode`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val exitCode = ExitCode.DefaultSuccess // Assuming this is defined elsewhere

        val sameOne = CommandResult(command, arguments, exitCode)
        val sameTwo = CommandResult(command, arguments, exitCode)
        val diffOne = CommandResult(command, arguments + "arg3", exitCode)

        assertEquals(sameOne, sameTwo)
        assertEquals(sameOne.hashCode(), sameTwo.hashCode())

        assertNotEquals(sameOne, diffOne)
        assertNotEquals(sameOne.hashCode(), diffOne.hashCode())

        assertNotEquals(sameTwo, diffOne)
        assertNotEquals(sameTwo.hashCode(), diffOne.hashCode())
    }
}
