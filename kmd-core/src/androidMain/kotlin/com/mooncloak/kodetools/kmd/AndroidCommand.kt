package com.mooncloak.kodetools.kmd

import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@OptIn(ExperimentalKmdApi::class)
internal actual suspend fun Command.execute(): CommandResult =
    withContext(Dispatchers.IO) {
        val commands = (command.commandToValues() + arguments.flatMap { argument ->
            argument.commandToValues()
        }).toTypedArray()

        var currentOutput = ProcessOutput()
        var currentError = ProcessOutput()

        var builder = ProcessBuilder(*commands)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = builder.redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
        }

        val process = builder.start()

        launch {
            process.inputStream.subscribeOutput(
                output = { currentOutput },
                onOutputChanged = { updated ->
                    currentOutput = updated

                    standardOutHandlers.forEach { handler ->
                        handler.handle(ProcessOutputScope, updated)
                    }
                }
            )
        }

        launch {
            process.errorStream.subscribeOutput(
                output = { currentError },
                onOutputChanged = { updated ->
                    currentError = updated

                    standardErrorHandlers.forEach { handler ->
                        handler.handle(ProcessOutputScope, updated)
                    }
                }
            )
        }

        val exitCode = ExitCode(value = process.waitFor())

        return@withContext CommandResult(
            command = command,
            arguments = arguments,
            exitCode = exitCode
        )
    }

private suspend fun InputStream.subscribeOutput(
    output: () -> ProcessOutput,
    onOutputChanged: suspend (ProcessOutput) -> Unit
) {
    withContext(Dispatchers.IO) {
        this@subscribeOutput.bufferedReader()
            .useLines { lineSequence ->
                val iterator = lineSequence.iterator()

                while (this.isActive && iterator.hasNext()) {
                    val line = iterator.next()

                    onOutputChanged(
                        ProcessOutput(
                            totalLines = output().totalLines + line,
                            diffLines = listOf(line)
                        )
                    )
                }
            }
    }
}
