package org.ivcode.gradle.resources

open class ResourcesExtension {

    /**
     * The package name for the loader. This package name should be something that's scanned by the spring application.
     * The generated resource loader will be placed in this package.
     */
    var configPackage: String? = null

    /**
     * The class name for the loader. This class should be something that doesn't conflict with other classes in the
     * package.
     */
    var configClass: String? = null

    /**
     * The directory containing the resources to be copied.
     */
    var resourcesDirectory: String? = null

    /**
     * The location within the jar where the resources will be copied. This should be something unique and isolated
     * within the project to avoid conflicts, to not overwrite file, or accidentally expose other resources.
     */
    var classpathDirectory: String? = null

    /**
     * The path to the resources. This is the path that will be used to access the resources from the web.
     * This is an ant-style path pattern. For example, "/resources/<code>**</code>" will match all resources in the /resources directory.
     *
     */
    var urlPattern: String? = null
}

internal fun ResourcesExtension.validate() {
    configPackage      = configPackage ?: error("loaderPackageName is required")
    configClass        = configClass ?: "PublicResourceConfigurer"
    resourcesDirectory = resourcesDirectory ?: error("resourceDirectory is required")
    classpathDirectory = classpathDirectory ?: error("classpathDirectory is required")
    urlPattern         = urlPattern ?: "/**"

    if (!classpathDirectory!!.startsWith("/") || !classpathDirectory!!.endsWith("/")) {
        error("classpathDirectory must start and end with a forward slash \"/\"")
    }

    if(!urlPattern!!.endsWith("**")) {
        error("urlPattern must end matching all files and directories with \"**\"")
    }
}
