package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalKmdApi::class)
class SudoTest {

    @Test
    fun `sudo get function returns sudo command`() = runTest {
        assertEquals(expected = listOf("sudo"), actual = sudo.get())
    }
}
