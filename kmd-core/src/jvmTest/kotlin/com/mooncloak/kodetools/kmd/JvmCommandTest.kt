package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalKmdApi::class)
class JvmCommandTest {

    @Test
    fun output_works() = runTest {
        // IDK how to test here. We have to mock up something or use an external service.
        // Just look at the logs when running the test and manually verify they look correct,
        // until we implement better tests.
        execKmd(
            command = "ls",
            onStandardOut = { output -> output.diffLines.forEach { line -> println(line) } },
            onStandardError = { error -> error.diffLines.forEach { line -> println("ERROR: $line") } }
        )
    }
}
