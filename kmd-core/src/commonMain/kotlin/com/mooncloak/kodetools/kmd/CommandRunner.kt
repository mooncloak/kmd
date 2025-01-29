package com.mooncloak.kodetools.kmd

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
