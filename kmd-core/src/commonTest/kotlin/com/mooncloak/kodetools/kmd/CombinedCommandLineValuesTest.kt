package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalKmdApi::class)
class CombinedCommandLineValuesTest {

    @Test
    fun `verify CombinedCommandLineValues construction`() = runTest {
        val valuesOne = CommandLineValues.invoke("one")
        val valuesTwo = CommandLineValues.invoke("two")
        val combinedValues = CombinedCommandLineValues(valuesOne, valuesTwo)

        assertEquals(expected = listOf("one"), actual = valuesOne.get())
        assertEquals(expected = listOf("two"), actual = valuesTwo.get())
        assertEquals(expected = listOf("one", "two"), actual = combinedValues.get())
    }

    @Test
    fun `verify plus function of CommandLineValues`() = runTest {
        val valuesOne = CommandLineValues.invoke("one")
        val valuesTwo = CommandLineValues.invoke("two")
        val combined = valuesOne + valuesTwo

        assertEquals(expected = listOf("one", "two"), actual = combined.get())
    }
}
