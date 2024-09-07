package org.ivcode.gradle.www

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.toFilePath
import java.io.File
import java.io.Writer


open class GenerateSourceTask: DefaultTask() {

    @TaskAction
    fun generate() {
        val extension = project.extensions.getByType(ResourcesExtension::class.java)

        val packageDir = File(project.getGeneratedSourceDirectory().asFile, extension.packageName!!.toFilePath())
        packageDir.mkdirs()

        val file = File(packageDir, "${extension.className!!}.java")
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