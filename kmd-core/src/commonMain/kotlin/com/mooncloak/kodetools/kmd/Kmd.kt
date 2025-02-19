package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope

/**
 * Constructs a new CommandBuilder for executing a given command with specified arguments and coroutine scope.
 *
 * This function utilizes the CommandBuilder class to configure a command execution pipeline.
 * The resulting CommandBuilder can be used to define specific execution behaviors, handle outputs, and chain
 * additional commands.
 *
 * @param [command] The primary command to be executed, provided as an arbitrary object.
 *
 * @param [arguments] A vararg list of additional arguments to be passed with the command.
 *
 * @param [coroutineScope] The coroutine scope within which the command will be executed. Defaults to MainScope.
 *
 * @param [onStandardOut] A handler for processing standard output during command execution.
 * Defaults to an empty handler.
 *
 * @param [onStandardError] A handler for processing standard error during command execution.
 * Defaults to an empty handler.
 *
 * @property [breakCommandOnWhitespace] Whether the [command] should be split to separate arguments with whitespace as
 * the delimiter. Defaults to `false`.
 *
 * @property [breakArgumentsOnWhitespace] Whether the [arguments] should be split to separate arguments with whitespace
 * as the delimiter. Defaults to `false.
 *
 * @return A Command configured with the provided values.
 */
@ExperimentalKmdApi
public fun kmd(
    command: Any,
    vararg arguments: Any,
    coroutineScope: CoroutineScope = MainScope(),
    onStandardOut: ProcessOutputHandler = ProcessOutputHandler {},
    onStandardError: ProcessOutputHandler = ProcessOutputHandler {},
    breakCommandOnWhitespace: Boolean = false,
    breakArgumentsOnWhitespace: Boolean = false
): Command = CommandBuilder(
    command = command,
    arguments = arguments.toList(),
    coroutineScope = coroutineScope,
    breakCommandOnWhitespace = breakCommandOnWhitespace,
    breakArgumentsOnWhitespace = breakArgumentsOnWhitespace
).onStandardOut(onStandardOut)
    .onStandardError(onStandardError)
    .build()

/**
 * Constructs a new CommandBuilder for executing a given command with specified arguments and coroutine scope.
 *
 * This function utilizes the CommandBuilder class to configure a command execution pipeline.
 * The resulting CommandBuilder can be used to define specific execution behaviors, handle outputs, and chain
 * additional commands.
 *
 * @param [command] The primary command to be executed, provided as an arbitrary object.
 *
 * @param [arguments] A vararg list of additional arguments to be passed with the command.
 *
 * @param [coroutineScope] The coroutine scope within which the command will be executed. Defaults to MainScope.
 *
 * @param [onStandardOut] A handler for processing standard output during command execution.
 * Defaults to an empty handler.
 *
 * @param [onStandardError] A handler for processing standard error during command execution.
 * Defaults to an empty handler.
 *
 * @property [breakCommandOnWhitespace] Whether the [command] should be split to separate arguments with whitespace as
 * the delimiter. Defaults to `false`.
 *
 * @property [breakArgumentsOnWhitespace] Whether the [arguments] should be split to separate arguments with whitespace
 * as the delimiter. Defaults to `false.
 *
 * @return A CommandBuilder configured with the provided values.
 */
@ExperimentalKmdApi
public fun kmdBuilder(
    command: Any,
    vararg arguments: Any,
    coroutineScope: CoroutineScope = MainScope(),
    onStandardOut: ProcessOutputHandler = ProcessOutputHandler {},
    onStandardError: ProcessOutputHandler = ProcessOutputHandler {},
    breakCommandOnWhitespace: Boolean = false,
    breakArgumentsOnWhitespace: Boolean = false
): CommandBuilder = CommandBuilder(
    command = command,
    arguments = arguments.toList(),
    coroutineScope = coroutineScope,
    breakCommandOnWhitespace = breakCommandOnWhitespace,
    breakArgumentsOnWhitespace = breakArgumentsOnWhitespace
).onStandardOut(onStandardOut)
    .onStandardError(onStandardError)

/**
 * Executes a specified command with given arguments and handles process outputs asynchronously.
 *
 * This function provides a coroutine-based interface for running commands, allowing
 * customization of standard output and error handling through supplied handlers.
 * The execution returns a detailed result, encapsulating the command, its arguments,
 * and the exit code.
 *
 * @param [command] The command to execute, represented as an arbitrary object.
 *
 * @param [arguments] Additional arguments to pass along with the command.
 *
 * @param [onStandardOut] A handler for processing standard output during command execution.
 * Defaults to an empty handler.
 *
 * @param [onStandardError] A handler for processing standard error during command execution.
 * Defaults to an empty handler.
 *
 * @property [breakCommandOnWhitespace] Whether the [command] should be split to separate arguments with whitespace as
 * the delimiter. Defaults to `false`.
 *
 * @property [breakArgumentsOnWhitespace] Whether the [arguments] should be split to separate arguments with whitespace
 * as the delimiter. Defaults to `false.
 *
 * @return A [CommandResult] representing the result of the command execution, including
 * the executed command, its arguments, and the exit code.
 */
@ExperimentalKmdApi
public suspend fun execKmd(
    command: Any,
    vararg arguments: Any,
    onStandardOut: ProcessOutputHandler = ProcessOutputHandler {},
    onStandardError: ProcessOutputHandler = ProcessOutputHandler {},
    breakCommandOnWhitespace: Boolean = false,
    breakArgumentsOnWhitespace: Boolean = false
): CommandResult = coroutineScope {
    kmd(
        command = command,
        arguments = arguments,
        coroutineScope = this@coroutineScope,
        onStandardOut = onStandardOut,
        onStandardError = onStandardError,
        breakCommandOnWhitespace = breakCommandOnWhitespace,
        breakArgumentsOnWhitespace = breakArgumentsOnWhitespace
    ).await()
}
