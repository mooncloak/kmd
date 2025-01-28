package com.mooncloak.kodetools.kmd

/**
 * Splits the current string into a list of substrings based on whitespace characters.
 *
 * This function uses a regular expression to match sequences of whitespace characters
 * and splits the string at each match. Consecutive whitespace characters are treated
 * as a single delimiter.
 *
 * @return A list of substrings obtained by splitting the string on whitespace. If the string
 * is empty or contains only whitespace, the result will be an empty list.
 */
internal fun String.splitOnWhiteSpace(): List<String> {
    if (this.isBlank()) return emptyList()

    return this.split("\\s+".toRegex()).filter { it.isNotBlank() }
}
