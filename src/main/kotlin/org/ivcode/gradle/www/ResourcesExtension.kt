package org.ivcode.gradle.www

import org.ivcode.gradle.www.util.toFilePath

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
    var classpath: String? = null

    /**
     * The path to the resources. This is the path that will be used to access the resources from the web.
     * This is an ant-style path pattern. For example, "/resources/<code>**</code>" will match all resources in the /resources directory.
     *
     */
    var url: String? = null
}

internal fun ResourcesExtension.validate() {
    packageName = packageName ?: error("packageName is required")
    className   = className ?: "PublicResourceConfigurer"
    resources   = resources ?: error("resources is required")
    classpath   = classpath ?: "/${packageName!!.toFilePath("/")}/www/"
    url         = url ?: "/**"

    if (!classpath!!.startsWith("/") || !classpath!!.endsWith("/")) {
        error("classpath must start and end with a forward slash \"/\"")
    }

    if(!url!!.endsWith("/**")) {
        error("url must end matching all files and directories with \"/**\"")
    }
}
