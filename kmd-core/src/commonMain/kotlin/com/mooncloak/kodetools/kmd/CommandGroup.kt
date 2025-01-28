package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Represents the aggregated results of executing a group of commands.
 *
 * This data class is used to encapsulate multiple `CommandResult` instances, providing a way
 * to manage and process the outcomes of executing a collection of commands. Each command's
 * execution result is recorded as an individual `CommandResult` within the `commands` list.
 *
 * @property commands A list of `CommandResult` objects representing the results of
 * executing the commands in the group.
 */
public data class CommandGroupResult public constructor(
    public val commands: List<CommandResult> = emptyList()
)

/**
 * Represents a group of commands that can be executed together either asynchronously or sequentially.
 *
 * This class provides mechanisms to execute a collection of commands using asynchronous and
 * reactive programming patterns. It aggregates the results of the commands into a
 * `CommandGroupResult` and allows for processing of individual command results as a flow.
 *
 * @property [commands] The list of commands to be executed as part of the group.
 */
public class CommandGroup internal constructor(
    @Suppress("MemberVisibilityCanBePrivate") public val commands: List<Command>,
    private val coroutineScope: CoroutineScope
) : AsyncExecutor<CommandGroupResult>,
    FlowExecutor<CommandResult> {

    override fun async(): Deferred<CommandGroupResult> =
        coroutineScope.async { executeAll() }

    override suspend fun await(): CommandGroupResult =
        executeAll()

    override fun flow(): Flow<CommandResult> = flow {
        commands.forEach { command ->
            emit(command.await())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandGroup) return false

        if (commands != other.commands) return false

        return coroutineScope == other.coroutineScope
    }

    override fun hashCode(): Int {
        var result = commands.hashCode()
        result = 31 * result + coroutineScope.hashCode()
        return result
    }

    override fun toString(): String =
        "CommandGroup(commands=$commands, coroutineScope=$coroutineScope)"

    public fun then(builder: CommandBuilder): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = this.commands + listOf(builder.build()),
            coroutineScope = coroutineScope
        )

    public fun then(other: Command): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = this.commands + listOf(other),
            coroutineScope = coroutineScope
        )

    public fun then(group: CommandGroup): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = this.commands + group.commands,
            coroutineScope = coroutineScope
        )

    public fun then(groupBuilder: CommandGroupBuilder): CommandGroupBuilder =
        CommandGroupBuilder(
            commands = this.commands + groupBuilder.build().commands,
            coroutineScope = coroutineScope
        )

    private suspend fun executeAll(): CommandGroupResult {
        val executedCommands = commands.map { command ->
            command.await()
        }

        return CommandGroupResult(
            commands = executedCommands
        )
    }
}

/**
 * A builder class for constructing a `CommandGroup` that aggregates multiple `CommandBuilder` instances.
 *
 * This class provides a fluent API for chaining multiple commands into a group and configuring
 * their collective behavior. Once constructed, a `CommandGroup` can be used to execute all
 * configured commands together, either sequentially or asynchronously.
 *
 * The `CommandGroupBuilder` implements the `OrderedConcatenation` interface, allowing users
 * to append additional `CommandBuilder` instances to the group in an ordered manner.
 *
 * @constructor Creates a new `CommandGroupBuilder` with an initial list of `CommandBuilder` instances
 * and a coroutine scope for asynchronous execution. This constructor is internal to ensure controlled instantiation.
 *
 * @param [commands] The initial list of `Command` instances to be included in the group.
 *
 * @param [coroutineScope] The coroutine scope used for executing the group of commands.
 */
public class CommandGroupBuilder internal constructor(
    commands: List<Command>,
    private val coroutineScope: CoroutineScope
) {

    private val commands = mutableListOf<Command>().apply {
        addAll(commands)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CommandGroupBuilder) return false

        if (coroutineScope != other.coroutineScope) return false

        return commands == other.commands
    }

    override fun hashCode(): Int {
        var result = coroutineScope.hashCode()
        result = 31 * result + commands.hashCode()
        return result
    }

    override fun toString(): String = "CommandGroupBuilder(coroutineScope=$coroutineScope, commands=$commands)"

    public fun then(other: Command): CommandGroupBuilder {
        commands.add(other)
        return this
    }

    public fun then(builder: CommandBuilder): CommandGroupBuilder {
        commands.add(builder.build())
        return this
    }

    public fun then(group: CommandGroup): CommandGroupBuilder {
        commands.addAll(group.commands)
        return this
    }

    public fun then(groupBuilder: CommandGroupBuilder): CommandGroupBuilder {
        commands.addAll(groupBuilder.build().commands)
        return this
    }

    public fun build(): CommandGroup = CommandGroup(
        commands = commands,
        coroutineScope = coroutineScope
    )
}
