package org.ivcode.gradle.www

import org.gradle.api.Project
import org.ivcode.gradle.www.util.asPackage
import org.ivcode.gradle.www.util.ifNullOrBlank
import org.ivcode.gradle.www.util.requireNotNullOrBlank

open class ResourcesExtension {

    /**
     * The package name for the loader. This package name should be something that's scanned by the spring application.
     * The generated resource loader will be placed in this package.
     */
    var packageName: String? = null

    /**
     * The class name for the loader. This class should be something that doesn't conflict with other classes in the
     * package.
     */
    var className: String? = null

    /**
     * The directory containing the resources to be copied.
     */
    var resources: String? = null

    /**
     * The location within the jar where the resources will be copied. This should be something unique and isolated
     * within the project to avoid conflicts, to not overwrite file, or accidentally expose other resources.
     */
    var resourcePath: String? = null

    /**
     * The path to the resources. This is the path that will be used to access the resources from the web.
     * This is an ant-style path pattern. For example, "/resources/<code>**</code>" will match all resources in the /resources directory.
     *
     */
    var url: String? = null

    /**
     * A name prefix for the properties. This is to uniquely identify the properties for this resource loader.
     */
    var propertyPrefix: String? = null
}

/**
 * Validates the resources extension and assigns default values where necessary.
 *
 * @param project the project
 */
internal fun ResourcesExtension.validate(project: Project) {
    resources = resources.requireNotNullOrBlank("resources is required")

    packageName = packageName.ifNullOrBlank {
        // If group or name is blank, then we don't need a separator
        val separator = if (project.group.toString().isNotBlank() && project.name.isNotBlank()) "." else ""
        "${project.group}$separator${project.name}".asPackage()
    }.requireNotNullOrBlank("packageName is required")

    className = className.ifNullOrBlank("PublicResourceConfigurer")

    resourcePath = resourcePath.ifNullOrBlank("/www/$packageName/")
    require(resourcePath!!.startsWith("/") && resourcePath!!.endsWith("/")) {
        "resourcePath must start and end with a forward slash \"/\": $resourcePath"
    }

    url = url.ifNullOrBlank("/**")
    require(url!!.endsWith("/**")) {
        "url must end matching all files and directories with \"/**\""
    }

    propertyPrefix = propertyPrefix
        .ifNullOrBlank(project.name)
        .requireNotNullOrBlank("propertyPrefix is required")
}
