package org.ivcode.gradle.www.util

import java.io.File

internal fun String.toFilePath(separator: String = File.separator): String {
    return replace(".", separator)
}

internal fun String.asPackage(): String =
    this
        .trim()
        .lowercase()
        .replace(Regex("[\\\\/]"), ".")
        .replace(Regex("[ -]"), "_")
        .replace(Regex("[^a-z0-9_\\\\.]"), "")
