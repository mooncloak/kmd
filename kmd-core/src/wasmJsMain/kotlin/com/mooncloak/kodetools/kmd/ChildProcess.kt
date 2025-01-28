@file:JsModule("child_process")

package com.mooncloak.kodetools.kmd

internal external fun exec(
    command: String,
    callback: (error: JsAny, stdout: JsString, stderr: JsString) -> Unit
)

internal external fun spawn(
    command: JsString,
    args: JsArray<JsString> = definedExternally
): ChildProcess

internal external interface Output : JsAny {

    fun on(event: JsString, callback: (data: JsAny) -> Unit): Unit
}

internal external interface ChildProcess : JsAny {

    val stdout: Output
    val stderr: Output

    fun on(event: JsString, callback: (data: JsAny) -> Unit): Unit
}
