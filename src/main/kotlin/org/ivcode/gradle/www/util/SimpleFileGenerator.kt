package org.ivcode.gradle.www.util

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import java.io.File
import java.io.Writer

/**
 * A simple file generator that uses Mustache templates.
 *
 * @property template The Mustache template path.
 * @property scope The data to be used in the template.
 * @property mustacheFactory The MustacheFactory instance.
 */
internal class SimpleFileGenerator (
    private val template: String,
    private val scope: Any,
    private val mustacheFactory: MustacheFactory = DefaultMustacheFactory()
) {

    /**
     * Writes the generated content to a file.
     *
     * @param file The target file.
     */
    fun write(file: File) = file.writer().use {
        write(it)
    }

    /**
     * Writes the generated content to a writer.
     *
     * @param writer The writer to which the content will be written.
     */
    private fun write(writer: Writer) {
        val m = mustacheFactory.compile(template)
        m.execute(writer, scope).flush()
    }
}