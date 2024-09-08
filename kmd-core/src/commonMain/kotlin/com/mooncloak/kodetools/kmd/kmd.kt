package com.mooncloak.kodetools.kmd

public fun kmd(
    command: String,
    vararg arguments: String,
    workingDirectory: String? = null
): Process = Process(
    command = command,
    additionalArguments = arguments.toList(),
    workingDirectory = workingDirectory
)
