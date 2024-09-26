package org.ivcode.gradle.www

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.getResourceDirectory
import org.ivcode.gradle.www.util.toFilePath
import java.io.File
import java.io.Writer

/**
 * Task to generate the source code
 */
open class GenerateSourceTask: DefaultTask() {

    @TaskAction
    fun generate() {
        val extension = project.extensions.getByType(ResourcesExtension::class.java)

        generateConfigurer(extension)
        generateSpringFactories(extension)
    }

    /**
     * Generates the configurer class
     */
    private fun generateConfigurer(extension: ResourcesExtension) {
        val packageDir = File(project.getGeneratedSourceDirectory().asFile, extension.packageName!!.toFilePath())
        packageDir.mkdirs()

        val configurerFile = File(packageDir, "${extension.className!!}.java")
        SimpleFileGenerator("mustache/configurer.mustache", extension).write(configurerFile)
    }

    /**
     * Generates the auto-config imports file
     */
    private fun generateSpringFactories(extension: ResourcesExtension) {
        val metaDir = File(project.getResourceDirectory().asFile, "META-INF/spring")
        metaDir.mkdirs()

        val springFactoriesFile = File(metaDir, "org.springframework.boot.autoconfigure.AutoConfiguration.imports")
        SimpleFileGenerator("mustache/imports.mustache", extension).write(springFactoriesFile)
    }
}

internal class SimpleFileGenerator (
    private val template: String,
    private val scope: Any
) {

    fun write(file: File) {
        file.writer().use { writer ->
            write(writer)
        }
    }

    private fun write(writer: Writer) {
        val mf: MustacheFactory = DefaultMustacheFactory()
        val m = mf.compile(template)

        m.execute(writer, scope).flush()
    }
}
