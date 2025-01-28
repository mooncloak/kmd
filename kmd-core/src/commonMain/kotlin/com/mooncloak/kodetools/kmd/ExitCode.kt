package com.mooncloak.kodetools.kmd

import kotlin.jvm.JvmInline

/**
 * Represents an operating system process exit code.
 *
 * This class encapsulates the exit code returned by a process and allows for well-defined handling of
 * success and failure codes. Exit codes are typically used to indicate the outcome of a command or
 * process execution.
 *
 * @property [value] The integer value of the exit code.
 *
 * The companion object provides predefined constants for standard exit codes, such as `DefaultSuccess`,
 * making it easier to work with commonly expected outcomes.
 */
@JvmInline
public value class ExitCode public constructor(
    public val value: Int
) {

    public companion object {

        /**
         * Represents a default exit code which indicates a successful process execution.
         *
         * This constant value is commonly used to verify that a command or operation completed successfully.
         * It is initialized with the standardized value of 0, which is traditionally recognized as a success
         * status in operating systems and process management.
         */
        public val DefaultSuccess: ExitCode = ExitCode(value = 0)

        /**
         * Represents a default exit code that indicates a general error in process execution.
         *
         * This value is standardized as 1, commonly used across operating systems to denote
         * that the process has failed due to an application error or unexpected condition.
         */
        public val DefaultError: ExitCode = ExitCode(value = 1)
    }
}

/**
 * Determines if the current exit code represents a success.
 *
 * @param [successCode] The exit code considered as a success, defaults to [ExitCode.DefaultSuccess].
 *
 * @return `true` if the current exit code matches the specified success code, otherwise `false`.
 */
public inline fun ExitCode.isSuccess(successCode: ExitCode = ExitCode.DefaultSuccess): Boolean =
    this.value == successCode.value

/**
 * Determines if the current exit code represents a failure.
 *
 * @param [successCode] The exit code considered as a success, defaults to [ExitCode.DefaultSuccess].
 *
 * @return `true` if the current exit code does not match the specified success code, otherwise `false`.
 */
public inline fun ExitCode.isFailure(successCode: ExitCode = ExitCode.DefaultSuccess): Boolean =
    !isSuccess(successCode)
