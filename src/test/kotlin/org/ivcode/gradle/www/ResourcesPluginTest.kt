package org.ivcode.gradle.www

import io.mockk.*
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskContainer
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.getResourceDirectory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ResourcesPluginTest {

    @BeforeEach
    fun setUp() {
        mockkStatic(ResourcesExtension::validate)
        mockkStatic(Project::getGeneratedSourceDirectory)
        mockkStatic(Project::getResourceDirectory)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(ResourcesExtension::validate)
        unmockkStatic(Project::getGeneratedSourceDirectory)
        unmockkStatic(Project::getResourceDirectory)
    }

    @Test
    fun `apply - java-library is applied`() {
        val project = mockk<Project>(relaxed = true)
        val plugin = ResourcesPlugin()

        plugin.apply(project)

        verify { project.plugins.apply("java-library") }
    }

    @Test
    fun `apply - www extensions is created`() {
        val project = mockk<Project>(relaxed = true)
        val plugin = ResourcesPlugin()

        plugin.apply(project)

        verify { project.extensions.create("www", ResourcesExtension::class.java) }
    }

    @Test
    fun `apply - adds dependencies`() {
        val project = mockk<Project>(relaxed = true)
        every { project.property("www.springVersion") } returns "V"

        val plugin = ResourcesPlugin()

        plugin.apply(project)

        verify { project.dependencies.add("implementation", "org.springframework:spring-webmvc:V") }
        verify { project.dependencies.add("implementation", "org.springframework:spring-context:V") }
    }

    @Test
    fun `afterEvaluate - resources extension is validated`() {
        val project = mockk<Project>(relaxed = true)
        val extensions = mockk<ExtensionContainer>(relaxed = true)
        val javaPluginExtension = mockk<JavaPluginExtension>(relaxed = true)
        val resourceExtension = mockk<ResourcesExtension>(relaxed = true)
        val taskContainer = mockk<TaskContainer>(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.getByType(JavaPluginExtension::class.java) } returns javaPluginExtension
        every { javaPluginExtension.sourceSets.getByName(any()) } returns mockk(relaxed = true)
        every { extensions.getByType(ResourcesExtension::class.java) } returns resourceExtension
        every { project.tasks } returns taskContainer
        every { project.getGeneratedSourceDirectory() } returns mockk(relaxed = true)
        every { project.getResourceDirectory() } returns mockk(relaxed = true)
        every { resourceExtension.validate(project) } returns Unit

        val plugin = ResourcesPlugin()
        plugin.apply(project)

        // Manually trigger afterEvaluate
        val afterEvaluateAction = slot<Action<Project>>()
        verify { project.afterEvaluate(capture(afterEvaluateAction)) }
        afterEvaluateAction.captured.execute(project)

        verify { resourceExtension.validate(project) }
    }

    @Test
    fun `afterEvaluate - source set is set the generated source directory`() {
        val project = mockk<Project>(relaxed = true)
        val extensions = mockk<ExtensionContainer>(relaxed = true)
        val javaPluginExtension = mockk<JavaPluginExtension>(relaxed = true)
        val sourceSet = mockk<SourceSet>(relaxed = true)
        val resourceExtension = mockk<ResourcesExtension>(relaxed = true)
        val taskContainer = mockk<TaskContainer>(relaxed = true)
        val generatedSourceDirectory = mockk<Directory>(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.getByType(JavaPluginExtension::class.java) } returns javaPluginExtension
        every { javaPluginExtension.sourceSets.getByName(any()) } returns sourceSet
        every { extensions.getByType(ResourcesExtension::class.java) } returns resourceExtension
        every { project.tasks } returns taskContainer
        every { project.getGeneratedSourceDirectory() } returns generatedSourceDirectory
        every { project.getResourceDirectory() } returns mockk(relaxed = true)
        every { resourceExtension.validate(project) } returns Unit

        val plugin = ResourcesPlugin()
        plugin.apply(project)

        // Manually trigger afterEvaluate
        val afterEvaluateAction = slot<Action<Project>>()
        verify { project.afterEvaluate(capture(afterEvaluateAction)) }
        afterEvaluateAction.captured.execute(project)

        verify { sourceSet.java.srcDir(generatedSourceDirectory) }
    }
}