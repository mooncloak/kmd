package com.mooncloak.kodetools.kmd

/**
 * Represents the output of a process, commonly utilized for standard output or error output.
 *
 * @property [type] The [ProcessOutputType] representing the stream this [ProcessOutput] came from.
 *
 * @property [totalLines] A list of strings representing the total output lines of the process.
 * The output can be either empty or populated with the captured lines.
 *
 * @property [diffLines] A list of strings representing the output lines of the process since the last emitted
 * [ProcessOutput] in a [ProcessOutputHandler].
 */
public data class ProcessOutput public constructor(
    public val type: ProcessOutputType,
    public val totalLines: List<String> = emptyList(),
    public val diffLines: List<String> = emptyList()
)

/**
 * Represents a scope for handling process output.
 *
 * This interface is predominantly utilized in conjunction with [ProcessOutputHandler]
 * to provide a structured context for processing and managing the output of a process.
 *
 * Implementations or usages of this interface can define specific behaviors for handling
 * and processing instances of [ProcessOutput], which typically encapsulate lines of
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
 * This handler enables customized processing of [ProcessOutput] objects
 * by defining a [handle] method within the context of `ProcessOutputScope`.
 * It facilitates the structured handling of process outputs, ensuring
 * controlled and reusable processing logic.
 *
 * The [handle] method must be implemented to define specific behavior
 * for handling `ProcessOutput`.
 */
public fun interface ProcessOutputHandler {

    /**
     * Handles process output within the scope of [ProcessOutputScope].
     *
     * This method is intended to process or transform the provided output
     * encapsulated in the [ProcessOutput] object in a structured and context-aware manner.
     *
     * @param [output] The process output to handle, represented as an instance of [ProcessOutput].
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

/**
 * A functional interface for handling individual lines of process output.
 *
 * This interface provides a mechanism for processing each line of a process output
 * in a structured and asynchronous manner. It extends [ProcessOutputHandler] by
 * defining a specific handling behavior for each line separately.
 *
 * The implementing function is expected to process or transform each line of the
 * [ProcessOutput.diffLines] property.
 *
 * [handleLine] must be overridden to define the custom handling logic for a single line of output,
 * while the default [handle] implementation ensures that each line is iteratively handled.
 */
public fun interface LineProcessOutputHandler : ProcessOutputHandler {

    /**
     * Handles a single line of process output.
     *
     * This function is invoked for each line of a process's output. Implementations should
     * define the logic for processing or transforming the given line as part of the output handling.
     *
     * @param [line] The line of output to be processed.
     */
    public suspend fun ProcessOutputScope.handleLine(type: ProcessOutputType, line: String)

    override suspend fun ProcessOutputScope.handle(output: ProcessOutput) {
        output.diffLines.forEach { line ->
            handleLine(
                type = output.type,
                line = line
            )
        }
    }

    public companion object
}

/**
 * Represents the type of process output, distinguishing between standard output and standard error.
 *
 * This enum is used in contexts where the handling or classification of process outputs
 * is necessary, such as capturing or routing the output produced by a command execution.
 *
 * - [STDOUT] corresponds to the standard output stream of a process.
 * - [STDERR] corresponds to the standard error stream of a process.
 */
public enum class ProcessOutputType {

    /**
     * Represents the standard output stream of a process.
     *
     * This constant is part of the [ProcessOutputType] enumeration and is used to classify
     * the standard output (stdout) stream. It is typically employed in contexts where
     * process output needs to be handled differently based on whether it is standard output
     * or standard error.
     */
    STDOUT,

    /**
     * Represents the standard error stream of a process.
     *
     * This constant is part of the [ProcessOutputType] enumeration and is used to classify
     * the standard error (stderr) stream. It is typically employed in contexts where
     * process output needs to be handled differently based on whether it is standard output
     * or standard error.
     */
    STDERR
}
