# kmd

Kotlin multiplatform shell command runner.

<img alt="GitHub tag (latest by date)" src="https://img.shields.io/github/v/tag/mooncloak/kmd">

```kotlin
execKmd("git checkout .")
```

## Status ⚠️

> [!Warning]
> This project is being actively developed but is in an early experimental state. Use the library
> cautiously and report back any issues. mooncloak is not responsible for any issues faced when
> using
> the library.

## Getting Started 🏁

Checkout the [releases page](https://github.com/mooncloak/kmd/releases) to get the latest version.
<br/><br/>
<img alt="GitHub tag (latest by date)" src="https://img.shields.io/github/v/tag/mooncloak/kmd">

### Repository

```kotlin
repositories {
    maven("https://repo.repsy.io/mvn/mooncloak/public")
}
```

### Dependencies

```kotlin
implementation("com.mooncloak.kodetools.kmd:kmd-core:VERSION")
```

## Usage

### Executing a command

```kotlin
val result = execKmd("git checkout .")

if (result.exitCode.isSuccessful()) {
    // Do something
}
```

### Listening to the process outputs

```kotlin
execKmd(
    command = "run some command",
    onStandardOut = { output ->
        println(output.diffLines.joinToString(separator = "\n"))
    },
    onStandardError = { error ->
        println("ERROR: ${error.diffLines.joinToString(separator = "\n")}")
    })
```

### Handle commands asynchronously

```kotlin
// Deferring the processing of the result
val deferred = kmd(
    command = "git checkout .",
    coroutineScope = myScope
).async()
val result = deferred.await()

// Suspend until the command is finished processing
val result = kmd("git checkout .")
    .await()

// Using a Flow to process the command result
kmd("git checkout .")
    .flow()
    .onEach { result -> println(result.exitCode.value) }
    .launchIn(coroutineScope)
```

### Building commands in fluent manner

```kotlin
kmdBuilder("git checkout .")
    .onStandardOut {}
    .onStandardError {}
    .build()
```

### Combining multiple commands to be processed synchronously

```kotlin
val checkout = kmdBuilder("git checkout .")
    .onStandardOut {}
    .onStandardError {}
    .build()

val status = kmdBuilder("git status")
    .onStandardOut {}
    .onStandardError {}
    .build()

checkout.then(status)
    .build()
    .await()
```

## Documentation 📃

More detailed documentation is available in the [docs](docs/) folder. The entry point to the
documentation can be
found [here](docs/index.md).

## Security 🛡️

For security vulnerabilities, concerns, or issues, please refer to
the [security policy](SECURITY.md) for more
information on appropriate approaches for disclosure.

## Contributing ✍️

Outside contributions are welcome for this project. Please follow
the [code of conduct](CODE_OF_CONDUCT.md)
and [coding conventions](CODING_CONVENTIONS.md) when contributing. If contributing code, please add
thorough documents
and tests. Thank you!

## License ⚖️

```
Copyright 2025 mooncloak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
