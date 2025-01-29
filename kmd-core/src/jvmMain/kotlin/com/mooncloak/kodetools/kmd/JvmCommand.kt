package com.mooncloak.kodetools.kmd

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.InputStream

@OptIn(ExperimentalKmdApi::class)
internal actual suspend fun Command.execute(): CommandResult =
    withContext(Dispatchers.IO) {
        val commands = (command.commandToValues() + arguments.flatMap { argument ->
            argument.commandToValues()
        }).toTypedArray()

        val process = ProcessBuilder(*commands)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        val standardOutJob = process.inputStream.toFlow(type = ProcessOutputType.STDOUT)
            .onEach { output ->
                standardOutHandlers.forEach { handler ->
                    handler.handle(ProcessOutputScope, output)
                }
            }
            .launchIn(this)

        val standardErrorJob = process.errorStream.toFlow(type = ProcessOutputType.STDERR)
            .onEach { output ->
                standardErrorHandlers.forEach { handler ->
                    handler.handle(ProcessOutputScope, output)
                }
            }
            .launchIn(this)

        val exitCode = ExitCode(value = process.waitFor())

        val result = CommandResult(
            command = command,
            arguments = arguments,
            exitCode = exitCode
        )

        standardOutJob.join()
        standardErrorJob.join()

        return@withContext result
    }

private fun InputStream.toFlow(type: ProcessOutputType): Flow<ProcessOutput> = flow {
    val context = currentCoroutineContext()

    var current = ProcessOutput(type = type)

    this@toFlow.bufferedReader()
        .useLines { lineSequence ->
            val iterator = lineSequence.iterator()

            while (context.isActive && iterator.hasNext()) {
                val line = iterator.next()

                val updated = current.copy(
                    totalLines = current.totalLines + line,
                    diffLines = listOf(line)
                )

                current = updated

                emit(updated)
            }
        }
}
