package org.ivcode.gradle.www.tasklet

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.gradle.api.Project
import org.gradle.api.Task
import org.ivcode.gradle.www.ResourcesExtension
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.getResourceDirectory
import org.ivcode.gradle.www.util.toFilePath
import java.io.File
import java.io.Writer

class GenerateSourceTasklet: Tasklet<Task> {

    override fun execute(task: Task) = with(task.project) {
        val extension = extensions.getByType(ResourcesExtension::class.java)

        generateConfigurer(this, extension)
        generateSpringFactories(this, extension)
    }

    private fun generateConfigurer(project: Project, extension: ResourcesExtension) {
        val packageDir = File(project.getGeneratedSourceDirectory().asFile, extension.packageName!!.toFilePath())
        ioMkdirs(packageDir)

        val configurerFile = File(packageDir, "${extension.className!!}.java")
        ioWrite(configurerFile, "mustache/configurer.mustache", extension)
    }

    private fun generateSpringFactories(project: Project, extension: ResourcesExtension) {
        val metaDir = File(project.getResourceDirectory().asFile, "META-INF/spring")
        ioMkdirs(metaDir)

        val springFactoriesFile = File(metaDir, "org.springframework.boot.autoconfigure.AutoConfiguration.imports")
        ioWrite(springFactoriesFile, "mustache/imports.mustache", extension)
    }

    internal fun ioWrite(to: File, template: String, extension: ResourcesExtension) {
        SimpleFileGenerator(template, extension).write(to)
    }

    internal fun ioMkdirs(file: File) {
        file.mkdirs()
    }
}

/**
 * A simple file generator that uses mustache templates
 */
internal class SimpleFileGenerator (
    private val template: String,
    private val scope: Any,
    private val mustacheFactory: MustacheFactory = DefaultMustacheFactory()
) {

    fun write(file: File) = file.writer().use {
        write(it)
    }

    private fun write(writer: Writer) {
        val m = mustacheFactory.compile(template)
        m.execute(writer, scope).flush()
    }
}