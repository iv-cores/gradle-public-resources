package org.ivcode.gradle.www.util

import org.gradle.api.Project
import org.gradle.api.file.Directory
import java.io.File.separator

/** The directory where generated source code is placed relative to the build directory */
private val generatedSourceDirectory = "generated${separator}sources${separator}main${separator}java"

/** The directory where resources are placed relative to the build directory */
private val resourceDirectory = "resources${separator}main"

/**
 * Gets the directory where generated source code is placed
 */
internal fun Project.getGeneratedSourceDirectory(): Directory =
    layout.buildDirectory.dir(generatedSourceDirectory).get()

/**
 * Gets the directory where resources are placed
 */
internal fun Project.getResourceDirectory(): Directory =
    layout.buildDirectory.dir(resourceDirectory).get()