@file:JsModule("child_process")
@file:JsNonModule

package com.mooncloak.kodetools.kmd

internal external fun exec(
    command: String,
    callback: (error: dynamic, stdout: String, stderr: String) -> Unit
)

internal external fun spawn(
    command: String,
    args: Array<String> = definedExternally
): ChildProcess

internal external interface Output {

    fun on(event: String, callback: (data: dynamic) -> Unit): Unit
}

internal external interface ChildProcess {

    val stdout: Output
    val stderr: Output

    fun on(event: String, callback: (data: dynamic) -> Unit): Unit
}
