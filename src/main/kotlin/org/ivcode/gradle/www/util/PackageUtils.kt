package org.ivcode.gradle.www.util

import java.io.File

internal fun String.toFilePath(separator: String = File.separator): String {
    return replace(".", separator)
}
