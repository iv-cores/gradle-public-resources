package org.ivcode.gradle.www.util

import org.gradle.api.Project
import org.gradle.api.file.Directory
import java.io.File.separator

private val generatedSourceDirectory = "generated${separator}sources${separator}main${separator}java"
private val resourceDirectory = "resources${separator}main"

internal fun Project.getGeneratedSourceDirectory(): Directory =
    layout.buildDirectory.dir(generatedSourceDirectory).get()

internal fun Project.getResourceDirectory(): Directory =
    layout.buildDirectory.dir(resourceDirectory).get()