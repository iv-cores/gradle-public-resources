package org.ivcode.gradle.resources.util

import java.io.File

internal fun String.toFilePath(separator: String = File.separator): String {
    return replace(".", separator)
}
