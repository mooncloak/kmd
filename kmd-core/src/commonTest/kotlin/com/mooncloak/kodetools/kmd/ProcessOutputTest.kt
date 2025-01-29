package com.mooncloak.kodetools.kmd

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ProcessOutputTest {

    @Test
    fun `verify ProcessOutput properties`() {
        val totalLines = listOf("Line1", "Line2", "Line3")
        val diffLines = listOf("Line2", "Line3")

        val processOutput = ProcessOutput(ProcessOutputType.STDOUT, totalLines, diffLines)

        assertEquals(totalLines, processOutput.totalLines)
        assertEquals(diffLines, processOutput.diffLines)
    }

    @Test
    fun `verify ProcessOutput equality and hashCode`() {
        val totalLines = listOf("Line1", "Line2", "Line3")
        val diffLines = listOf("Line2", "Line3")

        val sameOne = ProcessOutput(ProcessOutputType.STDOUT, totalLines, diffLines)
        val sameTwo = ProcessOutput(ProcessOutputType.STDOUT, totalLines, diffLines)
        val diffOne = ProcessOutput(ProcessOutputType.STDOUT, totalLines + "Line4", diffLines)

        assertEquals(sameOne, sameTwo)
        assertEquals(sameOne.hashCode(), sameTwo.hashCode())

        assertNotEquals(sameOne, diffOne)
        assertNotEquals(sameOne.hashCode(), diffOne.hashCode())

        assertNotEquals(sameTwo, diffOne)
        assertNotEquals(sameTwo.hashCode(), diffOne.hashCode())
    }

    @Test
    fun `verify ProcessOutput copy function`() {
        val totalLines = listOf("Line1", "Line2", "Line3")
        val diffLines = listOf("Line2", "Line3")

        val original = ProcessOutput(ProcessOutputType.STDOUT, totalLines, diffLines)
        val copiedWithSameValues = original.copy()
        val copiedWithChangedDiffLines = original.copy(diffLines = listOf("NewLine1", "NewLine2"))

        assertEquals(original, copiedWithSameValues)
        assertEquals(original.hashCode(), copiedWithSameValues.hashCode())

        assertNotEquals(original, copiedWithChangedDiffLines)
        assertNotEquals(original.diffLines, copiedWithChangedDiffLines.diffLines)
        assertEquals(totalLines, copiedWithChangedDiffLines.totalLines)
    }
}
