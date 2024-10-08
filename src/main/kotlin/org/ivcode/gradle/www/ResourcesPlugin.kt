package org.ivcode.gradle.www

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.ivcode.gradle.www.tasklet.CopyResourcesTasklet
import org.ivcode.gradle.www.tasklet.GenerateSourceTasklet
import org.ivcode.gradle.www.util.addExtraProperty
import org.ivcode.gradle.www.util.getGeneratedSourceDirectory
import org.ivcode.gradle.www.util.registerTasklet

internal const val TASK_GENERATE_SOURCE = "www-GenerateSources"
internal const val TASK_COPY_RESOURCES = "www-CopyResources"

class ResourcesPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.addExtraProperty("www.springVersion", "5.3.22")

        // Set up the plugin
        project.configPlugins()
        project.configExtensions()
        project.configTasks()
        project.configDependencies()

        // Validate the extensions after the project has been evaluated
        project.afterEvaluate {
            validateExtensions()
            configSourceSets()
        }
    }

    /**
     * Adds dependent plugins
     */
    private fun Project.configPlugins() {
        // This plugin ties into the java-library plugin
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
        // --== Generate Sources ==--
        // Register the GenerateSourceTasklet
        tasks.registerTasklet (
            TASK_GENERATE_SOURCE,
            GenerateSourceTasklet()
        )
        // Generate sources should run before compiling Java
        tasks.named("compileJava").configure {
            dependsOn(TASK_GENERATE_SOURCE)
        }

        // --== Copy Resources ==--
        // Register the CopyResourcesTasklet
        tasks.registerTasklet (
            TASK_COPY_RESOURCES,
            CopyResourcesTasklet()
        )
        // Copy resources before processing resources
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
     * Adds the necessary dependencies to compile the generated sources
     */
    private fun Project.configDependencies() {
        val springVersion = property("www.springVersion")!!

        // Note: The dependencies should not be transitive. Let the consuming project decide what version to use.
        dependencies.add("implementation", "org.springframework:spring-webmvc:$springVersion")
        dependencies.add("implementation", "org.springframework:spring-context:$springVersion")
    }
}
