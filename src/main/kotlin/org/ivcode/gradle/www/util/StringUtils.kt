package org.ivcode.gradle.www.util

fun String?.requireNotEmpty(msg: String = "value failed requirement to not be empty"): String {
    require(!this.isNullOrEmpty()) { msg }
    return this
}

fun String?.requireNotBlank(msg: String = "value failed requirement to not be blank"): String {
    require(!this.isNullOrBlank()) { msg }
    return this
}

fun String.ifNotBlank(block: (String) -> String): String {
    return if (this.isBlank()) {
        this
    } else {
        block(this)
    }
}