package org.ivcode.gradle.www

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.getResourceDirectory
import java.io.File

private const val TASK_GENERATE_SOURCE = "www-GenerateSources"
private const val TASK_COPY_RESOURCES = "www-CopyResources"

class ResourcesPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.configPlugins()
        project.configExtensions()
        project.configTasks()
        project.configDependencies()

        project.afterEvaluate {
            validateExtensions()
            configSourceSets()
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
        extensions.create("www", ResourcesExtension::class.java)
    }

    /**
     * Validates the plugin's extensions after the project has been evaluated
     */
    private fun Project.validateExtensions() {
        val extension = extensions.getByType(ResourcesExtension::class.java)
        extension.validate(this)
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

        // Copy resources to the resourcePath
        tasks.register<Copy>(TASK_COPY_RESOURCES) {
            val extension = project.extensions.getByType(ResourcesExtension::class.java)

            from(extension.resources)
            into(File(project.getResourceDirectory().asFile, extension.resourcePath!!))
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
