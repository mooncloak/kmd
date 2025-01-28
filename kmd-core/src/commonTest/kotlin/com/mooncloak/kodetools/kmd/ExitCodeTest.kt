package com.mooncloak.kodetools.kmd

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ExitCodeTest {

    @Test
    fun `ExitCode DefaultSuccess has correct code`() {
        assertEquals(0, ExitCode.DefaultSuccess.value)
    }

    @Test
    fun `ExitCode DefaultError has correct code`() {
        assertEquals(1, ExitCode.DefaultError.value)
    }

    @Test
    fun `ExitCode equality works correctly`() {
        val successCode = ExitCode(0)
        val errorCode = ExitCode(1)

        assertEquals(successCode, ExitCode.DefaultSuccess)
        assertEquals(errorCode, ExitCode.DefaultError)
        assertNotEquals(successCode, errorCode)
    }

    @Test
    fun `ExitCode isSuccess works correctly`() {
        val successCode = ExitCode(0)
        val errorCode = ExitCode(1)

        assertEquals(true, successCode.isSuccess())
        assertEquals(false, errorCode.isSuccess())
    }

    @Test
    fun `ExitCode isFailure works correctly`() {
        val successCode = ExitCode(0)
        val errorCode = ExitCode(1)

        assertEquals(false, successCode.isFailure())
        assertEquals(true, errorCode.isFailure())
    }
}
