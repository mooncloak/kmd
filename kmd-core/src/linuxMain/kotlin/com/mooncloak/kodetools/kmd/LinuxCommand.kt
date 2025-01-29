package com.mooncloak.kodetools.kmd

import kotlinx.cinterop.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.*
import platform.posix.*

// FIXME: Warning: Untested
@OptIn(ExperimentalKmdApi::class)
internal actual suspend fun Command.execute(): CommandResult =
    withContext(Dispatchers.Default) {
        val commands = (command.commandToValues() + arguments.flatMap { argument ->
            argument.commandToValues()
        }).toTypedArray()
        val commandToExecute = commands.joinToString(separator = " ")

        val buffer = Buffer()

        var currentOutput = ProcessOutput(type = ProcessOutputType.STDOUT)

        // TODO: Support stderr

        launch {
            buffer.subscribeOutput(
                output = { currentOutput },
                onOutputChanged = { updated ->
                    currentOutput = updated

                    standardOutHandlers.forEach { handler ->
                        handler.handle(ProcessOutputScope, updated)
                    }
                }
            )
        }

        val exitCode = ExitCode(
            value = executeNativeCommand(
                command = commandToExecute,
                sink = buffer
            )
        )

        return@withContext CommandResult(
            command = command,
            arguments = arguments,
            exitCode = exitCode
        )
    }

@OptIn(ExperimentalForeignApi::class)
@Throws(IllegalStateException::class)
private fun executeNativeCommand(
    command: String,
    sink: Sink
): Int {
    // Open the process using the given command
    val pipe = popen(command, __modes = "r")
        ?: error("Failed to run command: $command. Native 'popen' function returned null.")
    val exitCode: Int

    try {
        val buffer = ByteArray(4096) // Buffer to read command output
        memScoped {
            val cBuffer = allocArray<ByteVar>(buffer.size)
            while (fgets(cBuffer, buffer.size, pipe) != null) {
                sink.writeString(cBuffer.toKString())
            }
        }
    } finally {
        exitCode = pclose(pipe)
    }

    return exitCode
}

private suspend fun Source.subscribeOutput(
    output: () -> ProcessOutput,
    onOutputChanged: suspend (ProcessOutput) -> Unit
) {
    withContext(Dispatchers.Default) {
        val source = this@subscribeOutput

        var line = source.readLine()

        while (this.isActive && !source.exhausted() && line != null) {
            line = source.readLine()

            val current = output()
            val diffLines = listOfNotNull(line)

            onOutputChanged(
                current.copy(
                    totalLines = current.totalLines + diffLines,
                    diffLines = diffLines
                )
            )
        }
    }
}
