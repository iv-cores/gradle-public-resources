package org.ivcode.gradle.www.tasklet

import org.gradle.api.Project
import org.gradle.api.Task
import org.ivcode.gradle.www.ResourcesExtension
import org.ivcode.gradle.www.util.SimpleFileGenerator
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.getResourceDirectory
import org.ivcode.gradle.www.util.toFilePath
import java.io.File

/**
 * A tasklet that generates source files using Mustache templates.
 */
class GenerateSourceTasklet: Tasklet<Task> {

    /**
     * Configures the given Task to execute the generate source logic.
     *
     * @param task The Task to be configured.
     */
    override fun configuration(task: Task): Unit = with(task) {
        doLast {
            execute(this.project)
        }
    }

    /**
     * Executes the source generation logic.
     *
     * @param project The Gradle project.
     */
    fun execute(project: Project) = with(project) {
        val extension = extensions.getByType(ResourcesExtension::class.java)

        generateConfigurer(this, extension)
        generateSpringFactories(this, extension)
    }

    /**
     * Generates the configurer source file.
     *
     * @param project The Gradle project.
     * @param extension The ResourcesExtension containing configuration details.
     */
    private fun generateConfigurer(project: Project, extension: ResourcesExtension) {
        val packageDir = File(project.getGeneratedSourceDirectory().asFile, extension.packageName!!.toFilePath())
        ioMkDirs(packageDir)

        val configurerFile = File(packageDir, "${extension.className!!}.java")
        ioWrite(configurerFile, "mustache/configurer.mustache", extension)
    }

    /**
     * Generates the Spring factories file.
     *
     * @param project The Gradle project.
     * @param extension The ResourcesExtension containing configuration details.
     */
    private fun generateSpringFactories(project: Project, extension: ResourcesExtension) {
        val metaDir = File(project.getResourceDirectory().asFile, "META-INF/spring")
        ioMkDirs(metaDir)

        val springFactoriesFile = File(metaDir, "org.springframework.boot.autoconfigure.AutoConfiguration.imports")
        ioWrite(springFactoriesFile, "mustache/imports.mustache", extension)
    }

    /**
     * Writes content to a file using a Mustache template.
     *
     * @param to The target file.
     * @param template The Mustache template path.
     * @param extension The ResourcesExtension containing data for the template.
     */
    internal fun ioWrite(to: File, template: String, extension: ResourcesExtension) {
        SimpleFileGenerator(template, extension).write(to)
    }

    /**
     * Creates directories for the given file path.
     *
     * @param file The file path for which directories should be created.
     */
    internal fun ioMkDirs(file: File) {
        file.mkdirs()
    }
}

