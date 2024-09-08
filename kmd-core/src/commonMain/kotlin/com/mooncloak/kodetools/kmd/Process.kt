package com.mooncloak.kodetools.kmd

import okio.Sink
import okio.Source

interface Process {

    val isAlive: Boolean

    val standardOutput: Source
    
    val standardError: Source

    val standardInput: Sink

    companion object {

        /**
         * Represents a normal exit value from a finished process. By convention, this value is zero.
         */
        const val EXIT_VALUE_NORMAL = 0
    }
}

val Process.isTerminated: Boolean
    get() = !isAlive
