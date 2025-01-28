package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Represents the result of a command execution, encapsulating the command, its arguments,
 * and the resulting exit code.
 *
 * This data class is designed to provide a structured way to capture and handle the outcome
 * of a command execution. The combination of the executed command, its provided arguments,
 * and the associated exit code allows for detailed result analysis.
 *
 * @property [command] The command that was executed.
 *
 * @property [arguments] The list of arguments provided to the command during execution.
 *
 * @property [exitCode] The resulting exit code of the command execution, indicating success or failure.
 */
public data class CommandResult public constructor(
    public val command: Any,
    public val arguments: List<Any>,
    public val exitCode: ExitCode
)

/**
 * Represents a command to be executed with its associated arguments, output handlers, and execution context.
 *
 * This class encapsulates the mechanism for executing a command asynchronously or as a reactive stream,
 * providing a structured approach to manage the execution process and its results.
 *
 * @property [command] The actual command to execute, represented as an arbitrary object.
 *
 * @property [arguments] A list of arguments associated with the command.
 *
 * @property [standardOutHandlers] A list of handlers responsible for processing standard output.
 *
 * @property [standardErrorHandlers] A list of handlers responsible for processing standard error output.
 *
 * @constructor This class can only be constructed internally, ensuring controlled instantiation.
 */
public class Command internal constructor(
    public val command: Any,
    public val arguments: List<Any>,
    public val standardOutHandlers: List<ProcessOutputHandler>,
    public val standardErrorHandlers: List<ProcessOutputHandler>,
    private val coroutineScope: CoroutineScope
) : AsyncExecutor<CommandResult>,
    FlowExecutor<CommandResult> {

    override fun async(): Deferred<CommandResult> =
        coroutineScope.async { execute() }

    override suspend fun await(): CommandResult =
        execute()

    override fun flow(): Flow<CommandResult> = flow {
        emit(execute())
    }
}

/**
 * Executes the command asynchronously, processing its associated arguments,
 * output handlers, and execution context, and returns the result of the execution.
 *
 * @return A [CommandResult] representing the outcome of the command execution,
 * including the executed command, its arguments, and the resulting exit code.
 */
internal expect suspend fun Command.execute(): CommandResult

/**
 * A builder class for constructing and configuring a `Command` instance.
 *
 * This class provides a fluent API for defining commands, handling their outputs,
 * and chaining other CommandBuilders or CommandGroupBuilders using the `OrderedConcatenation` interface.
 *
 * @constructor
 * Creates a new `CommandBuilder` with the specified commands and coroutine scope.
 * The builder is intended for internal use.
 *
 * @property [command] The actual command to execute, represented as an arbitrary object.
 *
 * @param [arguments] The array of command elements to be executed.
 *
 * @param [coroutineScope] The coroutine scope used for asynchronous execution.
 */
public class CommandBuilder internal constructor(
    private val command: Any,
    private val arguments: List<Any>,
    private val coroutineScope: CoroutineScope
) : OrderedConcatenation<Command, CommandGroupBuilder> {

    private val mutableStandardOutHandlers = mutableListOf<ProcessOutputHandler>()
    private val mutableStandardErrorHandlers = mutableListOf<ProcessOutputHandler>()

    public fun onStandardOut(
        handler: ProcessOutputHandler
    ): CommandBuilder {
        mutableStandardOutHandlers.add(handler)
        return this
    }

    public fun onStandardError(
        handler: ProcessOutputHandler
    ): CommandBuilder {
        mutableStandardErrorHandlers.add(handler)
        return this
    }

    override fun then(other: Command): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this.build(),
                other
            ),
            coroutineScope = coroutineScope
        )

    public fun build(): Command = Command(
        command = command,
        arguments = arguments,
        standardOutHandlers = mutableStandardOutHandlers.toList(),
        standardErrorHandlers = mutableStandardErrorHandlers.toList(),
        coroutineScope = coroutineScope
    )
}
