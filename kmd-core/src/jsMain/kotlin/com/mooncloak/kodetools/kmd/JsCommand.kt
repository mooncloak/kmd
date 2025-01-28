package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// FIXME: Warning: Not Tested
@OptIn(ExperimentalKmdApi::class)
internal actual suspend fun Command.execute(): CommandResult =
    coroutineScope {
        val commands = (command.commandToValues() + arguments.flatMap { argument ->
            argument.commandToValues()
        }).toTypedArray()
        val formattedCommand = commands.first()
        val formattedArguments = commands.drop(1).toTypedArray()

        val outputMutex = Mutex(locked = false)
        val errorMutex = Mutex(locked = false)

        var currentOutput = ProcessOutput()
        var currentError = ProcessOutput()

        val code = executeNodeCommand(
            command = formattedCommand,
            arguments = formattedArguments,
            onStandardOutput = { output ->
                val updated = currentOutput.copy(
                    totalLines = currentOutput.totalLines + output,
                    diffLines = listOf(output)
                )
                currentOutput = updated

                standardOutHandlers.forEach { handler ->
                    launch {
                        outputMutex.withLock {
                            handler.handle(ProcessOutputScope, updated)
                        }
                    }
                }
            },
            onStandardError = { error ->
                val updated = currentError.copy(
                    totalLines = currentOutput.totalLines + error,
                    diffLines = listOf(error)
                )
                currentError = updated

                standardErrorHandlers.forEach { handler ->
                    launch {
                        errorMutex.withLock {
                            handler.handle(ProcessOutputScope, updated)
                        }
                    }
                }
            }
        )

        return@coroutineScope CommandResult(
            command = command,
            arguments = arguments,
            exitCode = ExitCode(value = code)
        )
    }

private suspend fun executeNodeCommand(
    command: String,
    arguments: Array<String>,
    onStandardOutput: (String) -> Unit,
    onStandardError: (String) -> Unit
): Int = suspendCoroutine { continuation ->
    val process = spawn(command, arguments)

    process.stdout.on("data") { data ->
        onStandardOutput(data.toString())
    }

    process.stderr.on("data") { data ->
        onStandardError(data.toString())
    }

    process.on("close") { code ->
        continuation.resume(code)
    }
}

/*
private suspend fun runShellCommand(command: String): Promise<String> = Promise { resolve, reject ->
    exec(command) { error, stdout, stderr ->
        if (error != null) {
            // Reject if there's an error
            reject(IllegalStateException(stderr))
        } else {
            // Resolve with the command output
            resolve(stdout)
        }
    }
}.await()
*/
