//[kmd-core](../../../index.md)/[com.mooncloak.kodetools.kmd](../index.md)/[Process](index.md)

# Process

[common]\
interface [Process](index.md)

Represents a native process that is created from running a command via the [kmd](../kmd.md) function.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [error](error.md) | [common]<br>abstract val [error](error.md): Source<br>Retrieves a Source representing the standard error output of this [Process](index.md). |
| [input](input.md) | [common]<br>abstract val [input](input.md): Sink<br>Retrieves a Sink representing the standard input of this [Process](index.md). |
| [isAlive](is-alive.md) | [common]<br>abstract val [isAlive](is-alive.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether this [Process](index.md) is currently active. |
| [isTerminated](../is-terminated.md) | [common]<br>val [Process](index.md).[isTerminated](../is-terminated.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether this process is terminated, either explicitly or through finishing normally. |
| [output](output.md) | [common]<br>abstract val [output](output.md): Source<br>Retrieves a Source representing the standard output of this [Process](index.md). |

## Functions

| Name | Summary |
|---|---|
| [await](await.md) | [common]<br>abstract suspend fun [await](await.md)(): [ExitCode](../-exit-code/index.md)<br>Waits until this [Process](index.md) terminates. |
| [terminate](terminate.md) | [common]<br>abstract fun [terminate](terminate.md)()<br>Terminates this process. |
