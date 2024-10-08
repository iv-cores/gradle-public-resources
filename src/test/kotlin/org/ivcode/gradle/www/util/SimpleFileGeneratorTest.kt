import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import io.mockk.*
import org.ivcode.gradle.www.util.SimpleFileGenerator
import org.ivcode.gradle.www.util.fileWriter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.io.OutputStreamWriter

class SimpleFileGeneratorTest {

    private val templatePath = "template.mustache"
    private val scope = mapOf("key" to "value")
    private val mustacheFactory: MustacheFactory = mockk()
    private val mustache: Mustache = mockk()

    @Test
    fun write_generatesFileContentCorrectly() {
        val file = mockk<File>(relaxed = true)
        val writer = mockk<OutputStreamWriter>(relaxed = true)

        mockkStatic("org.ivcode.gradle.www.util.FileUtilsKt")
        every { file.fileWriter() } returns writer
        every { mustacheFactory.compile(templatePath) } returns mustache
        every { mustache.execute(writer, scope) } returns writer

        val generator = SimpleFileGenerator(templatePath, scope, mustacheFactory)
        generator.write(file)

        verify { mustache.execute(writer, scope) }
        unmockkStatic("org.ivcode.gradle.www.util.FileUtilsKt")
    }

    @Test
    fun write_throwsExceptionWhenFileCannotBeWritten() {
        val file = mockk<File>(relaxed = true)

        mockkStatic("org.ivcode.gradle.www.util.FileUtilsKt")
        every { file.fileWriter() } throws RuntimeException("Cannot write to file")

        val generator = SimpleFileGenerator(templatePath, scope, mustacheFactory)

        assertThrows<RuntimeException> {
            generator.write(file)
        }
        unmockkStatic("org.ivcode.gradle.www.util.FileUtilsKt")
    }

    @Test
    fun writeToWriter_generatesContentCorrectly() {
        val writer = mockk<OutputStreamWriter>(relaxed = true)
        every { mustacheFactory.compile(templatePath) } returns mustache
        every { mustache.execute(writer, scope) } returns writer

        val generator = SimpleFileGenerator(templatePath, scope, mustacheFactory)
        generator.write(writer)

        verify { mustache.execute(writer, scope) }
    }

    @Test
    fun writeToWriter_throwsExceptionWhenTemplateNotFound() {
        val writer = mockk<OutputStreamWriter>(relaxed = true)
        every { mustacheFactory.compile(templatePath) } throws RuntimeException("Template not found")

        val generator = SimpleFileGenerator(templatePath, scope, mustacheFactory)

        assertThrows<RuntimeException> {
            generator.write(writer)
        }
    }
}