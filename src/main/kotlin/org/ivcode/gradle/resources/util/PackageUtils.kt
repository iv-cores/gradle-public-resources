package org.ivcode.gradle.resources.util

import java.io.File

internal fun String.toFilePath(): String {
    return replace(".", File.separator)
}