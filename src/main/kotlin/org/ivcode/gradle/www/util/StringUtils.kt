package org.ivcode.gradle.www.util

/**
 * Requires that the string is not null or blank.
 *
 * @param msg the message to use if the requirement fails
 * @return the string if it is not null or blank
 * @throws IllegalArgumentException if the string is null or blank
 */
fun String?.requireNotNullOrBlank(msg: String = "value failed requirement to not be blank"): String {
    require(!this.isNullOrBlank()) { msg }
    return this
}


/**
 * Returns the string if it is not null or blank, otherwise returns the default value.
 *
 * @param default the default value to return if the string is null or blank
 * @return the string if it is not null or blank, otherwise the default value
 */
fun String?.ifNullOrBlank(default: String): String =
    if (this.isNullOrBlank()) default else this

/**
 * Returns the string if it is not null or blank, otherwise returns the result of the block.
 *
 * @param block the block to execute if the string is null or blank
 * @return the string if it is not null or blank, otherwise the result of the block
 */
fun String?.ifNullOrBlank(block: () -> String): String =
    if (this.isNullOrBlank()) block() else this