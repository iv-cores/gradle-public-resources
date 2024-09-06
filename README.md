# gradle-www
A plugin to create static resource packages for application's using the spring-boot framework.

## Overview

This plugin configures and packages static resources, such as images, CSS, and JavaScript files, to be served by a
Spring-Boot application. The plugin creates a JAR file that contains the resources and the configuration logic to serve
them.

## Usage

settings.gradle.kts
```kotlin
// add the repository
pluginManagement {
    repositories {
        maven { url = uri("https://mvn.ivcode.org/mvn/snapshot") }
    }
}
```

build.gradle.kts
```kotlin
// apply the plugin
plugins {
  id("org.ivcode.gradle-www") version "<version>"
}

// configure the plugin
www {
  // The package name for the generated loader class
  // this should be a package that will be picked up by Spring-Boot's component scanning
  packageName = "org.myapp.resources"
    
  // The location of your static resources
  resources   = "${projectDir}/www"
}

// if resources need to be built, make the copy task dependent on that (optional)
named("www-CopyResources").configure {
    dependsOn("build-resources")
}
```

## Tasks
The plugin pulls in the `java-library` plugin and adds the following tasks:

### Generate Sources
 - name: `www-GenerateSources`
 - executes before: `compileJava`

This task generates the source code that will be used to configure Spring-Boot to serve the resources. When configuring
keep in mind the package name. To simplify your application configuration, the package name should be something that
will be picked up by Spring-Boot's component scanning.


### Copy Resources
 - name: `www-CopyResources`
 - executes before: `processResources`

This task copies the resources from the specified directory to the gradle build directory so that it can be packaged
into the JAR file.

If the resources need to be built or processed in any way before copying, make this task dependent on those tasks.

## Configuration

The plugin is configured using the `www` extension. The following properties are available:

| Property    | Description                                              | Default                    |
|-------------|----------------------------------------------------------|----------------------------|
| packageName | The package name for the generated loader class          | *required*                 |
| className   | The class name for the generated loader class            | `PublicResourceConfigurer` |
| resources   | The directory containing the static resources to package | *required*                 |
| classpath   | The destination within the package for these resources   | `/${packageName}/www/`     |
| url         | The URL path to serve the resources from                 | `/**`                      |

## Security Considerations
It is possible this plugin could leak sensitive information. To avoid this, public resources should be isolated. The
target classpath location, the location where these resources will live within the package, should never contain other
files. Those resources would inadvertently be available to the public.