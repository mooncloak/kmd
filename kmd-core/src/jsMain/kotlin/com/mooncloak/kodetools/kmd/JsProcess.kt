package com.mooncloak.kodetools.kmd

internal actual operator fun Process.Companion.invoke(
    command: String,
    additionalArguments: List<String>,
    workingDirectory: String?
): Process = throw NotImplementedError("Process is not yet implemented for target 'JavaScript'.")
