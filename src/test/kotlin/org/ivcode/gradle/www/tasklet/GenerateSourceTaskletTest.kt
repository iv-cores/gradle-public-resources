package org.ivcode.gradle.www.tasklet

import io.mockk.*
import org.gradle.api.Project
import org.gradle.api.Task
import org.ivcode.gradle.www.ResourcesExtension
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.getResourceDirectory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Unit tests for the GenerateSourceTasklet class.
 */
class GenerateSourceTaskletTest {

    private lateinit var project: Project
    private lateinit var task: Task
    private lateinit var extension: ResourcesExtension
    private lateinit var tasklet: GenerateSourceTasklet

    /**
     * Sets up the test environment before each test.
     * Mocks the necessary dependencies and ensures that file system operations are not executed.
     */
    @BeforeEach
    fun setup() {

        // depended-on components
        project = mockk()
        task = mockk()
        extension = mockk()

        // system under test
        tasklet = spyk(GenerateSourceTasklet())

        // Mock the project and extension
        every { task.project } returns project
        every { project.extensions.getByType(ResourcesExtension::class.java) } returns extension

        every { project.getGeneratedSourceDirectory() } returns mockk {
            every { asFile } returns File("build/generated")
        }

        every { project.getResourceDirectory() } returns mockk {
            every { asFile } returns File("build/resources")
        }

        // Make sure the tasklet doesn't actually write to the file system
        every { tasklet.ioWrite(any(), any(), any()) } just Runs
        every { tasklet.ioMkdirs(any()) } just Runs
    }


    /**
     * Tests that the execute method generates the necessary sources and verifies that
     * the ioMkdirs and ioWrite methods are called the correct number of times with the correct parameters.
     */
    @Test
    fun `execute - verify that all sources will be generated`() {
        every { extension.packageName } returns "com.example"
        every { extension.className } returns "MyConfigurer"

        tasklet.execute(task)

        // Configurer
        verify(exactly = 1) { tasklet.ioMkdirs(
            File("build/generated/com/example")
        )}
        verify(exactly = 1) { tasklet.ioWrite(
            File("build/generated/com/example/MyConfigurer.java"),
            "mustache/configurer.mustache",
            extension
        )}

        // Spring Factories
        verify(exactly = 1) { tasklet.ioMkdirs(
            File("build/resources/META-INF/spring")
        )}
        verify(exactly = 1) { tasklet.ioWrite(
            File("build/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"),
            "mustache/imports.mustache",
            extension
        )}
    }
}