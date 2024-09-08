package com.mooncloak.kodetools.kmd

import okio.Sink
import okio.Source

/**
 * Represents a native process that is created from running a command via the [kmd] function.
 */
public interface Process {

    /**
     * Whether this [Process] is currently active.
     */
    public val isAlive: Boolean

    /**
     * Retrieves a [Source] representing the standard output of this [Process].
     */
    public val output: Source

    /**
     * Retrieves a [Source] representing the standard error output of this [Process].
     */
    public val error: Source

    /**
     * Retrieves a [Sink] representing the standard input of this [Process].
     */
    public val input: Sink

    /**
     * Terminates this process.
     */
    public fun terminate()

    /**
     * Waits until this [Process] terminates.
     *
     * @return The [ExitCode] of the process.
     */
    public suspend fun await(): ExitCode

    public companion object
}

/**
 * Whether this process is terminated, either explicitly or through finishing normally.
 */
public val Process.isTerminated: Boolean
    inline get() = !isAlive

internal expect operator fun Process.Companion.invoke(
    command: String,
    additionalArguments: List<String> = emptyList(),
    workingDirectory: String? = null
): Process
