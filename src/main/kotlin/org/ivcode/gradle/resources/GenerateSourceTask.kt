package org.ivcode.gradle.resources

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.ivcode.gradle.resources.util.getGeneratedSourceDirectory
import org.ivcode.gradle.resources.util.toFilePath
import java.io.File
import java.io.Writer


open class GenerateSourceTask: DefaultTask() {

    @TaskAction
    fun generate() {
        val extension = project.extensions.getByType(ResourcesExtension::class.java)

        val packageDir = File(project.getGeneratedSourceDirectory().asFile, extension.configPackage!!.toFilePath())
        packageDir.mkdirs()

        val file = File(packageDir, "${extension.configClass!!}.java")
        SimpleFileGenerator("mustache/loader.mustache", extension).write(file)
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

    fun write(writer: Writer) {
        val mf: MustacheFactory = DefaultMustacheFactory()
        val m = mf.compile(template)

        m.execute(writer, scope).flush()
    }
}