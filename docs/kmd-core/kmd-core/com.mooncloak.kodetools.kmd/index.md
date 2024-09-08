//[kmd-core](../../index.md)/[com.mooncloak.kodetools.kmd](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [ExitCode](-exit-code/index.md) | [common]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [ExitCode](-exit-code/index.md)(val value: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [Process](-process/index.md) | [common]<br>interface [Process](-process/index.md)<br>Represents a native process that is created from running a command via the [kmd](kmd.md) function. |

## Properties

| Name | Summary |
|---|---|
| [isTerminated](is-terminated.md) | [common]<br>val [Process](-process/index.md).[isTerminated](is-terminated.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Whether this process is terminated, either explicitly or through finishing normally. |

## Functions

| Name | Summary |
|---|---|
| [kmd](kmd.md) | [common]<br>fun [kmd](kmd.md)(command: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), vararg arguments: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), workingDirectory: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [Process](-process/index.md) |
