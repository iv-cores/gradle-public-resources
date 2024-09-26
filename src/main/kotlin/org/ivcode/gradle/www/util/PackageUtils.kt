package org.ivcode.gradle.www.util

import java.io.File

/**
 * Converts a package name into to a file path.
 *
 * @param separator the separator to use, defaults to [File.separator]
 * @return the string as a file path
 */
internal fun String.toFilePath(separator: String = File.separator): String {
    return replace(".", separator)
}

/**
 * Converts a string to a package name.
 *
 * This is a process of converting what's given to a valid package name.
 *
 * The following rules are applied:
 * - The string is lowercased
 * - Slashes and backslashes are replaced with dots
 * - Spaces and hyphens are replaced with underscores
 * - All invalid characters removed
 *
 * @return the string as a package name
 * @throws IllegalArgumentException if the resulting string is blank
 */
internal fun String.asPackage(): String =
    this.trim()
        .lowercase()
        .replace(Regex("[\\\\/]"), ".") // replace slashes with dots
        .replace(Regex("\\.{2,}"), ".") // replace multiple dots with a single dot
        .replace(Regex("[ -]"), "_") // replace spaces and hyphens with underscores
        .replace(Regex("_{2,}"), "_") // replace multiple underscores with a single underscore
        .replace(Regex("^[_.]*"), "") // remove leading dots and underscores
        .replace(Regex("[_.]*$"), "") // remove trailing dots and underscores
        .replace(Regex("[^a-z0-9_.]"), "") // remove invalid characters
        .requireNotNullOrBlank("invalid package name")