package com.mooncloak.kodetools.kmd

/**
 * Represents a static command-line value provider for the `sudo` command.
 *
 * The `sudo` object is a specialized implementation of the `CommandLineValues` interface.
 * It provides a predefined list of arguments, specifically containing the `sudo` command.
 * This enables seamless integration where the `sudo` command is required in a
 * command-line execution context.
 *
 * Annotated with `@ExperimentalKmdApi`, this feature is marked as experimental, and
 * its usage requires explicit opt-in acknowledgment.
 *
 * This object implements the `get` function of the `CommandLineValues` interface,
 * ensuring asynchronous retrieval of the predefined command-line value.
 */
@Suppress("ClassName")
@ExperimentalKmdApi
public data object sudo : CommandLineValues {

    override suspend fun get(): List<String> = listOf("sudo")
}
