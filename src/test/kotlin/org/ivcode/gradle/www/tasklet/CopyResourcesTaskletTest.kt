package org.ivcode.gradle.www.tasklet

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.ivcode.gradle.www.ResourcesExtension
import org.ivcode.gradle.www.util.getResourceDirectory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class CopyResourcesTaskletTest {

    @Test
    fun `configuration - copies resources to the correct directory`() {
        val copyTask: Copy = mockk(relaxed = true)
        val extension: ResourcesExtension = mockk {
            every { resources } returns "src/main/resources"
            every { resourcePath } returns "static"
        }
        val project: Project = mockk {
            every { extensions.getByType(ResourcesExtension::class.java) } returns extension
            every { getResourceDirectory() } returns mockk {
                every { asFile } returns File("build/resources")
            }
        }
        every { copyTask.project } returns project

        val tasklet = CopyResourcesTasklet()
        tasklet.configuration(copyTask)

        verify { copyTask.from("src/main/resources") }
        verify { copyTask.into(File("build/resources/static")) }
    }

    @Test
    fun `configuration - handles null resourcePath with exception`() {
        val copyTask: Copy = mockk(relaxed = true)
        val extension: ResourcesExtension = mockk {
            every { resources } returns "src/main/resources"
            every { resourcePath } returns null
        }
        val project: Project = mockk {
            every { extensions.getByType(ResourcesExtension::class.java) } returns extension
            every { getResourceDirectory() } returns mockk {
                every { asFile } returns File("build/resources")
            }
        }
        every { copyTask.project } returns project

        val tasklet = CopyResourcesTasklet()
        assertThrows<IllegalArgumentException> {
            tasklet.configuration(copyTask)
        }
    }

    @Test
    fun `configuration - throws exception when resources are not specified`() {
        val copyTask: Copy = mockk(relaxed = true)
        val extension: ResourcesExtension = mockk {
            every { resources } returns null
            every { resourcePath } returns "static"
        }
        val project: Project = mockk {
            every { extensions.getByType(ResourcesExtension::class.java) } returns extension
            every { getResourceDirectory() } returns mockk {
                every { asFile } returns File("build/resources")
            }
        }
        every { copyTask.project } returns project

        val tasklet = CopyResourcesTasklet()
        assertThrows<IllegalArgumentException> {
            tasklet.configuration(copyTask)
        }
    }
}