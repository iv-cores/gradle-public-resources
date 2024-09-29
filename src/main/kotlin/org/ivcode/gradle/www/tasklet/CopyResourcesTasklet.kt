package org.ivcode.gradle.www.tasklet

import org.gradle.api.tasks.Copy
import org.ivcode.gradle.www.ResourcesExtension
import org.ivcode.gradle.www.util.getResourceDirectory
import java.io.File

/**
 * A tasklet that copies resources to the build directory
 */
class CopyResourcesTasklet: Tasklet<Copy> {

    /**
     * Configures the given Copy task to copy resources from the source directory
     * specified in the ResourcesExtension to the target directory.
     *
     * @param task The Copy task to be configured.
     */
    override fun configuration(task: Copy): Unit = with(task) {
        val extension = project.extensions.getByType(ResourcesExtension::class.java)

        from(extension.resources)
        into(File(project.getResourceDirectory().asFile, extension.resourcePath!!))
    }
}