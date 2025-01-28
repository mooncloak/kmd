package com.mooncloak.kodetools.kmd

/**
 * Represents the output of a process, commonly utilized for standard output or error output.
 *
 * @property [totalLines] A list of strings representing the total output lines of the process.
 * The output can be either empty or populated with the captured lines.
 *
 * @property [diffLines] A list of strings representing the output lines of the process since the last emitted
 * [ProcessOutput] in a [ProcessOutputHandler].
 *
 * This class is primarily used to encapsulate and transfer process output data in
 * conjunction with classes like `CommandProcess`. It ensures a structured approach
 * to handling and parsing process output across various parts of the system.
 */
public data class ProcessOutput public constructor(
    public val totalLines: List<String> = emptyList(),
    public val diffLines: List<String> = emptyList()
)

/**
 * Represents a scope for handling process output.
 *
 * This interface is predominantly utilized in conjunction with `ProcessOutputHandler`
 * to provide a structured context for processing and managing the output of a process.
 *
 * Implementations or usages of this interface can define specific behaviors for handling
 * and processing instances of `ProcessOutput`, which typically encapsulate lines of
 * output produced by an executed process.
 *
 * The companion object serves as a convenient implementation of this interface,
 * enabling direct and reusable access to the scope.
 */
public interface ProcessOutputScope {

    public companion object : ProcessOutputScope
}

/**
 * A functional interface for handling process output within a given scope.
 *
 * This handler enables customized processing of `ProcessOutput` objects
 * by defining a `handle` method within the context of `ProcessOutputScope`.
 * It facilitates the structured handling of process outputs, ensuring
 * controlled and reusable processing logic.
 *
 * The `handle` method must be implemented to define specific behavior
 * for handling `ProcessOutput`.
 */
public fun interface ProcessOutputHandler {

    /**
     * Handles process output within the scope of `ProcessOutputScope`.
     *
     * This method is intended to process or transform the provided output
     * encapsulated in the `ProcessOutput` object in a structured and context-aware manner.
     *
     * @param [output] The process output to handle, represented as an instance of `ProcessOutput`.
     */
    public suspend fun ProcessOutputScope.handle(output: ProcessOutput)

    public companion object
}

/**
 * A convenience function for invoking the [ProcessOutputHandler.handle] function. This avoids having to scope the call
 * to a [ProcessOutputScope], which instead can be provided as a parameter.
 *
 * @see [ProcessOutputHandler.handle]
 */
internal suspend fun ProcessOutputHandler.handle(scope: ProcessOutputScope, output: ProcessOutput) {
    scope.handle(output)
}
