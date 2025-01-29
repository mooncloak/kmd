package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.CoroutineScope

/**
 * Interface that facilitates the execution of commands and provides functionalities to handle the resulting outputs.
 *
 * This interface defines methods to execute commands ensuring a consistent and modular approach to managing command
 * execution and results. The commands can be specified as a structured [Command] object or through dynamic input with
 * arguments, which are internally transformed into a [Command].
 *
 * Experimental API: This interface is marked as experimental and may undergo changes in the future.
 */
@ExperimentalKmdApi
public interface CommandRunner {

    /**
     * Executes a given command and returns the result of the execution.
     *
     * The method takes a structured [Command] instance, executes it asynchronously,
     * and provides the corresponding [CommandResult].
     *
     * @param [command] The [Command] to be executed, which contains the command logic,
     * its arguments, and associated output handlers.
     *
     * @return The [CommandResult] which includes details about the executed command,
     * its arguments, and the resulting exit code.
     */
    public suspend fun run(command: Command): CommandResult

    /**
     * Executes a command with the provided arguments and returns its result.
     *
     * This method constructs a [Command] instance using the given command and arguments,
     * and then executes it asynchronously. The resulting execution details are encapsulated
     * in a [CommandResult].
     *
     * @param [command] The command to be executed. Can be any object representing the command logic.
     *
     * @param [arguments] Additional arguments to be passed to the command.
     *
     * @return The result of the command execution as a [CommandResult], which includes the executed
     * command, its provided arguments, and the resulting exit code.
     */
    public suspend fun run(
        command: Any,
        vararg arguments: Any
    ): CommandResult {
        val cmd = kmd(
            command = command,
            arguments = arguments
        )

        return this.run(command = cmd)
    }

    public companion object
}

/**
 * Creates and returns a new instance of [DefaultCommandRunner] with the specified configurations.
 *
 * This operator function provides a convenient way to initialize a [CommandRunner] with customized
 * output handlers, whitespace handling options, and coroutine scope for asynchronous operations.
 *
 * @param [standardOutHandlers] A list of [ProcessOutputHandler] instances to handle the standard output
 * of the executed commands.
 *
 * @param [standardErrorHandlers] A list of [ProcessOutputHandler] instances to handle the standard error
 * of the executed commands.
 *
 * @param [breakCommandOnWhitespace] Indicates whether the command should be split by whitespace into
 * separate arguments before execution. Defaults to false.
 *
 * @param [breakArgumentsOnWhitespace] Indicates whether the arguments should be split by whitespace
 * before execution. Defaults to false.
 *
 * @param [coroutineScope] The [CoroutineScope] used to manage the lifecycle of asynchronous command execution.
 *
 * @return A new instance of [DefaultCommandRunner] configured with the provided options.
 */
@ExperimentalKmdApi
public operator fun CommandRunner.Companion.invoke(
    standardOutHandlers: List<ProcessOutputHandler>,
    standardErrorHandlers: List<ProcessOutputHandler>,
    breakCommandOnWhitespace: Boolean = false,
    breakArgumentsOnWhitespace: Boolean = false,
    coroutineScope: CoroutineScope
): DefaultCommandRunner = DefaultCommandRunner(
    standardOutHandlers = standardOutHandlers,
    standardErrorHandlers = standardErrorHandlers,
    breakCommandOnWhitespace = breakCommandOnWhitespace,
    breakArgumentsOnWhitespace = breakArgumentsOnWhitespace,
    coroutineScope = coroutineScope
)

/**
 * Implementation of the [CommandRunner] interface that executes commands with a shared execution context.
 *
 * This class provides a structured approach to running commands asynchronously by enabling configuration
 * of how command outputs are handled, how commands and arguments are processed, and the coroutine scope
 * used for execution.
 *
 * @constructor Creates a new instance of [DefaultCommandRunner] with the specified configurations.
 *
 * @param [standardOutHandlers] A list of [ProcessOutputHandler]s to handle the standard output of the executed
 * command.
 *
 * @param [standardErrorHandlers] A list of [ProcessOutputHandler]s to handle the standard error of the executed
 * command.
 *
 * @param [breakCommandOnWhitespace] Indicates whether the command should be split by whitespace into separate
 * arguments before execution. Defaults to false.
 *
 * @param [breakArgumentsOnWhitespace] Indicates whether arguments should be split by whitespace before execution.
 * Defaults to false.
 *
 * @param coroutineScope The [CoroutineScope] used for managing the lifecycle of asynchronous command execution.
 */
@ExperimentalKmdApi
public class DefaultCommandRunner internal constructor(
    private val standardOutHandlers: List<ProcessOutputHandler>,
    private val standardErrorHandlers: List<ProcessOutputHandler>,
    private val breakCommandOnWhitespace: Boolean = false,
    private val breakArgumentsOnWhitespace: Boolean = false,
    private val coroutineScope: CoroutineScope
) : CommandRunner {

    override suspend fun run(command: Command): CommandResult {
        val updatedCommand = command.toBuilder()
            .onStandardOut(standardOutHandlers)
            .onStandardError(standardErrorHandlers)
            .breakCommandOnWhitespace(breakCommandOnWhitespace)
            .breakArgumentsOnWhitespace(breakArgumentsOnWhitespace)
            .withCoroutineScope(coroutineScope)
            .build()

        return updatedCommand.await()
    }
}
