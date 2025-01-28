package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CommandBuilderTest {

    @Test
    fun `verify CommandBuilder properties`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val coroutineScope = CoroutineScope(Job())

        val builder = CommandBuilder(command, arguments, coroutineScope)

        assertEquals(command, builder.build().command)
        assertEquals(arguments, builder.build().arguments)
    }

    @Test
    fun `verify CommandBuilder equality`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val coroutineScope = CoroutineScope(Job())

        val builderOne = CommandBuilder(command, arguments, coroutineScope)
        val builderTwo = CommandBuilder(command, arguments, coroutineScope)
        val builderDifferent = CommandBuilder("differentCommand", arguments, coroutineScope)

        assertEquals(builderOne.build(), builderTwo.build())
        assertNotEquals(builderOne.build(), builderDifferent.build())
    }

    @Test
    fun `verify onStandardOut adds handlers`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val coroutineScope = CoroutineScope(Job())
        val handler = ProcessOutputHandler { }

        val builder = CommandBuilder(command, arguments, coroutineScope).onStandardOut(handler)

        assertTrue(builder.build().standardOutHandlers.contains(handler))
    }

    @Test
    fun `verify onStandardError adds handlers`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val coroutineScope = CoroutineScope(Job())
        val handler = ProcessOutputHandler { }

        val builder = CommandBuilder(command, arguments, coroutineScope).onStandardError(handler)

        assertTrue(builder.build().standardErrorHandlers.contains(handler))
    }

    @Test
    fun `verify build creates Command`() {
        val command = "testCommand"
        val arguments = listOf("arg1", "arg2")
        val coroutineScope = CoroutineScope(Job())

        val builder = CommandBuilder(command, arguments, coroutineScope)
        val builtCommand = builder.build()

        assertEquals(command, builtCommand.command)
        assertEquals(arguments, builtCommand.arguments)
        assertTrue(builtCommand.standardOutHandlers.isEmpty())
        assertTrue(builtCommand.standardErrorHandlers.isEmpty())
    }
}
