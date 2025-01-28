package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalKmdApi::class)
class StringConstantCommandLineValuesTest {

    @Test
    fun `verify StringConstantCommandLineValues constructor functions`() = runTest {
        val valuesOne = CommandLineValues.invoke("one")
        val valuesMultiple = CommandLineValues.invoke(listOf("one", "two", "three"))

        assertEquals(expected = listOf("one"), actual = valuesOne.get())
        assertEquals(expected = listOf("one", "two", "three"), actual = valuesMultiple.get())
    }

    @Test
    fun `verify StringConstantCommandLineValues values`() = runTest {
        val stringConstant = StringConstantCommandLineValues(values = listOf("one", "two"))

        assertEquals(expected = listOf("one", "two"), actual = stringConstant.get())
    }

    @Test
    fun `verify StringConstantCommandLineValues equality and hashCode`() {
        // Assuming `StringConstantCommandLineValues` overrides equals/hashCode.
        val name = "testName"
        val value = "testValue"

        val sameOne = StringConstantCommandLineValues(listOf(name, value))
        val sameTwo = StringConstantCommandLineValues(listOf(name, value))
        val diffOne = StringConstantCommandLineValues(listOf(name, "differentValue"))

        assertEquals(sameOne, sameTwo)
        assertEquals(sameOne.hashCode(), sameTwo.hashCode())

        assertNotEquals(sameOne, diffOne)
        assertNotEquals(sameOne.hashCode(), diffOne.hashCode())
    }
}
