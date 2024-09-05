package org.ivcode.gradle.resources

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.ivcode.gradle.resources.util.getGeneratedSourceDirectory
import org.ivcode.gradle.resources.util.getResourceDirectory
import java.io.File

private const val TASK_GENERATE_SOURCE = "generateStaticLoader"
private const val TASK_COPY_RESOURCES = "copyStaticResources"

class ResourcesPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.configPlugins()
        project.configExtensions()
        project.configDependencies()
        project.configSourceSets()
        project.configTasks()
        project.afterEvaluate {
            validateExtensions()
        }
    }

    /**
     * Adds dependent plugins
     */
    private fun Project.configPlugins() {
        // Depends on the java-library plugin
        plugins.apply("java-library")
    }

    /**
     * Adds the plugin's extensions
     */
    private fun Project.configExtensions() {
        extensions.create("resources", ResourcesExtension::class.java)
    }

    /**
     * Validates the plugin's extensions after the project has been evaluated
     */
    private fun Project.validateExtensions() {
        val extension = extensions.getByType(ResourcesExtension::class.java)
        extension.validate()
    }

    /**
     * Adds the necessary tasks for the plugin
     */
    private fun Project.configTasks() {
        // Generate the source code
        tasks.register(
            TASK_GENERATE_SOURCE,
            GenerateSourceTask::class
        )

        // Copy resources to the classpath
        tasks.register<Copy>(TASK_COPY_RESOURCES) {
            val extension = project.extensions.getByType(ResourcesExtension::class.java)

            from(extension.resourcesDirectory)
            into(File(project.getResourceDirectory().asFile, extension.classpathDirectory!!))
        }

        // Tie the tasks into the build process
        tasks.named("compileJava").configure {
            dependsOn(TASK_GENERATE_SOURCE)
        }
        tasks.named("processResources").configure {
            dependsOn(TASK_COPY_RESOURCES)
        }
    }

    /**
     * Adds the generated source directory to the project
     */
    private fun Project.configSourceSets() {
        val javaExtension = extensions.getByType(JavaPluginExtension::class.java)
        javaExtension.sourceSets.getByName("main").java.srcDir(getGeneratedSourceDirectory())
    }

    /**
     * Adds the necessary dependencies for the generated source
     */
    private fun Project.configDependencies() {
        dependencies.add("implementation", "org.springframework:spring-webmvc:5.3.22")
        dependencies.add("implementation", "org.springframework:spring-context:5.3.22")
    }
}