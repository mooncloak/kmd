@file:Suppress("MemberVisibilityCanBePrivate")

package com.mooncloak.kodetools.kmd.buildSrc

object LibraryConstants {

    const val group = "com.mooncloak.kodetools.kmd"
    const val owner = "mooncloak.kodetools"
    const val repoName = "kmd"
    const val versionName = "1.0.0-SNAPSHOT"
    const val versionCode = 1
    const val versionDescription = "Release $versionName ($versionCode)"
    const val license = "Apache-2.0"
    const val vcsUrl = "https://github.com/mooncloak/kmd.git"

    object Android {

        const val compileSdkVersion = 33
        const val minSdkVersion = 21
        const val targetSdkVersion = 33
    }
}
