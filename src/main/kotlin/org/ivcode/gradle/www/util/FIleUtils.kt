@file:JvmName("FileUtilsKt")

package org.ivcode.gradle.www.util

import java.io.File
import java.io.Writer



/**
 * A wrapper for the [File.fileWriter] method so that it can be mocked.
 */
fun File.fileWriter(): Writer = this.writer()