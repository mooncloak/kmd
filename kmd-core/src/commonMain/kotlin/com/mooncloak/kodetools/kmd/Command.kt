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
 * @property [breakCommandOnWhitespace] Whether the [command] should be split to separate arguments with whitespace as
 * the delimiter. Defaults to `false`.
 *
 * @property [breakArgumentsOnWhitespace] Whether the [arguments] should be split to separate arguments with whitespace
 * as the delimiter. Defaults to `false.
 *
 * @property [coroutineScope] The [CoroutineScope] instance that will be used to launch deferred loading of results.
 *
 * @constructor This class can only be constructed internally, ensuring controlled instantiation.
 */
public class Command internal constructor(
    public val command: Any,
    public val arguments: List<Any>,
    public val standardOutHandlers: List<ProcessOutputHandler>,
    public val standardErrorHandlers: List<ProcessOutputHandler>,
    public val breakCommandOnWhitespace: Boolean = false,
    public val breakArgumentsOnWhitespace: Boolean = false,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Command) return false

        if (command != other.command) return false
        if (arguments != other.arguments) return false
        if (standardOutHandlers != other.standardOutHandlers) return false
        if (standardErrorHandlers != other.standardErrorHandlers) return false
        if (breakCommandOnWhitespace != other.breakCommandOnWhitespace) return false
        if (breakArgumentsOnWhitespace != other.breakArgumentsOnWhitespace) return false

        return coroutineScope == other.coroutineScope
    }

    override fun hashCode(): Int {
        var result = command.hashCode()
        result = 31 * result + arguments.hashCode()
        result = 31 * result + standardOutHandlers.hashCode()
        result = 31 * result + standardErrorHandlers.hashCode()
        result = 31 * result + coroutineScope.hashCode()
        result = 31 * result + breakCommandOnWhitespace.hashCode()
        result = 31 * result + breakArgumentsOnWhitespace.hashCode()
        return result
    }

    override fun toString(): String =
        "Command(command=$command, arguments=$arguments, standardOutHandlers=$standardOutHandlers, standardErrorHandlers=$standardErrorHandlers, breakCommandOnWhitespace=$breakCommandOnWhitespace, breakArgumentsOnWhitespace=$breakArgumentsOnWhitespace, coroutineScope=$coroutineScope)"

    public fun then(builder: CommandBuilder): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this,
                builder.build()
            ),
            coroutineScope = coroutineScope
        )

    public fun then(other: Command): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this,
                other
            ),
            coroutineScope = coroutineScope
        )

    public fun then(group: CommandGroup): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this
            ) + group.commands,
            coroutineScope = coroutineScope
        )

    public fun then(groupBuilder: CommandGroupBuilder): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this
            ) + groupBuilder.build().commands,
            coroutineScope = coroutineScope
        )

    /**
     * Converts this [Command] instance into a [CommandBuilder].
     *
     * This method creates a new [CommandBuilder] object by copying the properties of the
     * current [Command] instance, allowing modifications and reconfiguration of the command.
     *
     * @return A new [CommandBuilder] instance initialized with the properties of the current [Command].
     */
    public fun toBuilder(): CommandBuilder =
        CommandBuilder(
            command = command,
            arguments = arguments,
            coroutineScope = coroutineScope,
            breakCommandOnWhitespace = breakCommandOnWhitespace,
            breakArgumentsOnWhitespace = breakArgumentsOnWhitespace
        )
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
 *
 * @property [breakCommandOnWhitespace] Whether the [command] should be split to separate arguments with whitespace as
 * the delimiter. Defaults to `false`.
 *
 * @property [breakArgumentsOnWhitespace] Whether the [arguments] should be split to separate arguments with whitespace
 * as the delimiter. Defaults to `false.
 */
public class CommandBuilder internal constructor(
    private val command: Any,
    private val arguments: List<Any>,
    private var coroutineScope: CoroutineScope,
    private var breakCommandOnWhitespace: Boolean = false,
    private var breakArgumentsOnWhitespace: Boolean = false
) {

    private val mutableStandardOutHandlers = mutableListOf<ProcessOutputHandler>()
    private val mutableStandardErrorHandlers = mutableListOf<ProcessOutputHandler>()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandBuilder) return false

        if (command != other.command) return false
        if (arguments != other.arguments) return false
        if (coroutineScope != other.coroutineScope) return false
        if (mutableStandardOutHandlers != other.mutableStandardOutHandlers) return false
        if (breakCommandOnWhitespace != other.breakCommandOnWhitespace) return false
        if (breakArgumentsOnWhitespace != other.breakArgumentsOnWhitespace) return false

        return mutableStandardErrorHandlers == other.mutableStandardErrorHandlers
    }

    override fun hashCode(): Int {
        var result = command.hashCode()
        result = 31 * result + arguments.hashCode()
        result = 31 * result + coroutineScope.hashCode()
        result = 31 * result + mutableStandardOutHandlers.hashCode()
        result = 31 * result + mutableStandardErrorHandlers.hashCode()
        result = 31 * result + breakCommandOnWhitespace.hashCode()
        result = 31 * result + breakArgumentsOnWhitespace.hashCode()
        return result
    }

    override fun toString(): String =
        "CommandBuilder(command=$command, arguments=$arguments, coroutineScope=$coroutineScope, breakCommandOnWhitespace=$breakCommandOnWhitespace, breakArgumentsOnWhitespace=$breakArgumentsOnWhitespace, mutableStandardOutHandlers=$mutableStandardOutHandlers, mutableStandardErrorHandlers=$mutableStandardErrorHandlers)"

    public fun onStandardOut(
        handler: ProcessOutputHandler
    ): CommandBuilder {
        mutableStandardOutHandlers.add(handler)
        return this
    }

    public fun onStandardOut(
        handlers: Collection<ProcessOutputHandler>
    ): CommandBuilder {
        mutableStandardOutHandlers.addAll(handlers)
        return this
    }

    public fun onStandardError(
        handler: ProcessOutputHandler
    ): CommandBuilder {
        mutableStandardErrorHandlers.add(handler)
        return this
    }

    public fun onStandardError(
        handlers: Collection<ProcessOutputHandler>
    ): CommandBuilder {
        mutableStandardErrorHandlers.addAll(handlers)
        return this
    }

    public fun breakCommandOnWhitespace(value: Boolean): CommandBuilder {
        this.breakCommandOnWhitespace = value
        return this
    }

    public fun breakArgumentsOnWhitespace(value: Boolean): CommandBuilder {
        this.breakArgumentsOnWhitespace = value
        return this
    }

    public fun withCoroutineScope(coroutineScope: CoroutineScope): CommandBuilder {
        this.coroutineScope = coroutineScope
        return this
    }

    public fun then(builder: CommandBuilder): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this.build(),
                builder.build()
            ),
            coroutineScope = coroutineScope
        )

    public fun then(other: Command): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this.build(),
                other
            ),
            coroutineScope = coroutineScope
        )

    public fun then(group: CommandGroup): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this.build()
            ) + group.commands,
            coroutineScope = coroutineScope
        )

    public fun then(groupBuilder: CommandGroupBuilder): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = listOf(
                this.build()
            ) + groupBuilder.build().commands,
            coroutineScope = coroutineScope
        )

    public fun build(): Command = Command(
        command = command,
        arguments = arguments,
        standardOutHandlers = mutableStandardOutHandlers.toList(),
        standardErrorHandlers = mutableStandardErrorHandlers.toList(),
        breakCommandOnWhitespace = breakCommandOnWhitespace,
        breakArgumentsOnWhitespace = breakArgumentsOnWhitespace,
        coroutineScope = coroutineScope
    )
}
