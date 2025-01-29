package com.mooncloak.kodetools.kmd

import android.os.Build
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.InputStream

@OptIn(ExperimentalKmdApi::class)
internal actual suspend fun Command.execute(): CommandResult =
    withContext(Dispatchers.IO) {
        val commands = (command.commandToValues(splitOnWhitespace = breakCommandOnWhitespace) +
                arguments.flatMap { argument ->
                    argument.commandToValues(splitOnWhitespace = breakArgumentsOnWhitespace)
                }).toTypedArray()

        var builder = ProcessBuilder(*commands)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = builder.redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
        }

        val process = builder.start()

        var standardOutJob: Job? = null
        var standardErrorJob: Job? = null

        // TODO: Handle output on Android before API 24?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            standardOutJob = process.inputStream.toFlow(type = ProcessOutputType.STDOUT)
                .onEach { output ->
                    standardOutHandlers.forEach { handler ->
                        handler.handle(ProcessOutputScope, output)
                    }
                }
                .launchIn(this)

            standardErrorJob = process.errorStream.toFlow(type = ProcessOutputType.STDERR)
                .onEach { output ->
                    standardErrorHandlers.forEach { handler ->
                        handler.handle(ProcessOutputScope, output)
                    }
                }
                .launchIn(this)
        }

        val exitCode = ExitCode(value = process.waitFor())

        val result = CommandResult(
            command = command,
            arguments = arguments,
            exitCode = exitCode
        )

        standardOutJob?.join()
        standardErrorJob?.join()

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
