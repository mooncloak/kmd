package com.mooncloak.kodetools.kmd

import kotlin.jvm.JvmInline

@JvmInline
public value class ExitCode public constructor(
    public val value: Int
) {

    public companion object {

        /**
         * Represents a normal exit value from a finished process. By convention, this value is zero.
         */
        public val NORMAL: ExitCode = ExitCode(value = 0)
    }
}
