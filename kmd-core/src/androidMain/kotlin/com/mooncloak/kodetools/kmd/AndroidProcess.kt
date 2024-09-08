package com.mooncloak.kodetools.kmd

import android.annotation.TargetApi
import android.os.Build
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.supervisorScope
import okio.Sink
import okio.Source
import okio.sink
import okio.source
import java.io.File

@TargetApi(Build.VERSION_CODES.O)
internal actual operator fun Process.Companion.invoke(
    command: String,
    additionalArguments: List<String>,
    workingDirectory: String?
): Process {
    val fullCommand = command.split(' ') + additionalArguments

    val process = ProcessBuilder(*fullCommand.toTypedArray())
        .directory(workingDirectory?.let { File(it) })
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    return AndroidProcess(process = process)
}

@TargetApi(Build.VERSION_CODES.O)
internal class AndroidProcess internal constructor(
    private val process: java.lang.Process
) : Process {

    override val isAlive: Boolean
        get() = process.isAlive

    override val output: Source = process.inputStream.source()

    override val error: Source = process.errorStream.source()

    override val input: Sink = process.outputStream.sink()

    override fun terminate() {
        process.destroy()
    }

    override suspend fun await(): ExitCode {
        supervisorScope {
            while (this.isActive && process.isAlive) {
                delay(10)
            }
        }

        val exitValue = try {
            process.exitValue()
        } catch (_: IllegalThreadStateException) {
            -1
        }

        return ExitCode(value = exitValue)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AndroidProcess) return false

        return process == other.process
    }

    override fun hashCode(): Int = process.hashCode()

    override fun toString(): String =
        "AndroidProcess(process=$process)"
}
