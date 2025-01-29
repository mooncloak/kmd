package com.mooncloak.kodetools.kmd

/**
 * Represents a functional interface for retrieving a list of command-line values asynchronously.
 *
 * The `CommandLineValues` interface defines a single abstract suspend function `get`,
 * which is used to fetch command-line values. Implementations of this interface enable
 * asynchronous fetching of arguments or command values.
 *
 * This [CommandLineValues] component allows for building type-safe command line values. For example, using Kotlin's
 * infix functions, and this component, you can define your own type-safe commands.
 *
 * @see [sudo] for a custom [CommandLineValues] implementation.
 */
@ExperimentalKmdApi
public fun interface CommandLineValues {

    /**
     * Retrieves a list of command-line values asynchronously.
     *
     * This function is designed to be implemented by a `CommandLineValues` functional interface,
     * allowing for the asynchronous resolution of a list of string arguments from the command line
     * or a similar asynchronous data source.
     *
     * @return A list of strings representing the asynchronously resolved command-line values.
     */
    public suspend fun get(): List<String>

    public companion object
}

/**
 * Creates a new instance of `CommandLineValues` using a fixed list of string values.
 *
 * This function allows the creation of a `CommandLineValues` instance that consistently returns
 * the provided list of string values when the `get` function is called. It is particularly useful
 * for scenarios where command-line arguments are known and are not dynamically resolved.
 *
 * @param [values] The list of strings representing the command-line values to be encapsulated.
 *
 * @return A `CommandLineValues` instance that returns the specified list of string values.
 */
@ExperimentalKmdApi
public operator fun CommandLineValues.Companion.invoke(values: List<String>): CommandLineValues =
    StringConstantCommandLineValues(values = values)

/**
 * Creates a `CommandLineValues` instance that wraps a single string value.
 *
 * This operator function simplifies the creation of a `CommandLineValues` instance
 * where the provided string value is used as a constant command-line argument.
 *
 * @param [value] A string representing the command-line value to be wrapped.
 *
 * @return A `CommandLineValues` instance containing the specified string value as its command-line argument.
 */
@ExperimentalKmdApi
public operator fun CommandLineValues.Companion.invoke(value: String): CommandLineValues =
    StringConstantCommandLineValues(values = listOf(value))

/**
 * Combines two instances of `CommandLineValues` into a single instance.
 *
 * This operator function takes two `CommandLineValues` instances and creates a new combined
 * instance that sequentially resolves values from both. The resulting instance, when invoked,
 * will retrieve and concatenate values from the first and second `CommandLineValues` instances.
 *
 * @param [other] Another instance of `CommandLineValues` to be combined with the current instance.
 *
 * @return A new `CommandLineValues` instance that combines the values from the current instance
 * and the provided `other` instance.
 */
@ExperimentalKmdApi
public operator fun CommandLineValues.plus(other: CommandLineValues): CommandLineValues =
    CombinedCommandLineValues(
        first = this,
        second = other
    )

/**
 * Converts the current command or argument object into a list of string values that can be executed in a command line
 * processor.
 *
 * If the object is an instance of `CommandLineValues`, it retrieves the
 * list of values asynchronously using the `get` function. Otherwise,
 * it converts the object to a string and splits it into substrings
 * based on whitespace characters.
 *
 * @return A list of strings derived from the current object. If the object
 * is a `CommandLineValues`, the resulting list is obtained asynchronously
 * through the `get` function. If the object is not a `CommandLineValues`,
 * the resulting list is obtained by converting the object to a string
 * and splitting it on whitespace.
 */
@ExperimentalKmdApi
internal suspend fun Any.commandToValues(splitOnWhitespace: Boolean = false): List<String> =
    if (this is CommandLineValues) {
        this.get()
    } else if (splitOnWhitespace) {
        this.toString().splitOnWhiteSpace()
    } else {
        listOf(this.toString())
    }

@ExperimentalKmdApi
internal class StringConstantCommandLineValues internal constructor(
    private val values: List<String>
) : CommandLineValues {

    override suspend fun get(): List<String> = values

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StringConstantCommandLineValues) return false

        return values == other.values
    }

    override fun hashCode(): Int = values.hashCode()

    override fun toString(): String = "StringConstantCommandLineValues(values=$values)"
}

@ExperimentalKmdApi
internal class CombinedCommandLineValues internal constructor(
    private val first: CommandLineValues,
    private val second: CommandLineValues
) : CommandLineValues {

    override suspend fun get(): List<String> =
        first.get() + second.get()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CombinedCommandLineValues) return false

        if (first != other.first) return false

        return second == other.second
    }

    override fun hashCode(): Int {
        var result = first.hashCode()
        result = 31 * result + second.hashCode()
        return result
    }

    override fun toString(): String = "CombinedCommandLineValues(first=$first, second=$second)"
}
