# gradle-www
A plugin to create static resource packages for applications using the Spring Boot framework.

## Overview
This plugin configures and packages static resources, such as images, CSS, and JavaScript files, to be served by a
Spring Boot application. It is useful for packaging a front-end application with a back-end application. The plugin
creates a JAR file that contains the resources and autoconfigures the application to serve those resources within the
Spring Boot application.

## Usage

### Setup

In your `settings.gradle.kts` file, add the plugin repository:
```kotlin
// add the repository
pluginManagement {
    repositories {
        maven { url = uri("https://mvn.ivcode.org/mvn/snapshot") }
    }
}
```

Then, in your build.gradle.kts file, apply and configure the plugin:
```kotlin
// apply the plugin
plugins {
    id("org.ivcode.gradle-www") version "<version>"
}

// configure the plugin
www {
    // The location of the static resources to package
    resources = "${projectDir}/www"
}

// if resources need to be built, make the copy task dependent on that (optional)
named("www-CopyResources").configure {
    dependsOn("build-resources")
}

```

### Build

To build the package, run the following command:

```shell
./gradlew build
```

## Tasks
The plugin pulls in the `java-library` plugin and adds the following tasks:

### Generate Sources
 - name: `www-GenerateSources`
 - executes before: `compileJava`

This task generates the artifacts needed to autoconfigure the application and serve the static resources.


### Copy Resources
 - name: `www-CopyResources`
 - executes before: `processResources`

This task copies the resources from the specified directory to the gradle build directory so that it can be packaged
into the JAR file.

If the resources need to be built or processed in any way before copying, make this task dependent on those tasks.

## Configuration

The plugin is configured using the `www` extension. The following properties are available:

| Property      | Description                                              | Default                    |
|---------------|----------------------------------------------------------|----------------------------|
| resources     | The directory containing the static resources to package | *required*                 |
| packageName   | The package name for the generated loader class          | `${group}.${name}`         |
| className     | The class name for the generated loader class            | `PublicResourceConfigurer` |
| resourcePath  | The destination within the package for these resources   | `/www/${packageName}/`     |
| url           | The URL path to serve the resources from                 | `/**`                      |

## Security Considerations
It is possible this plugin could leak sensitive information. To avoid this, public resources should be isolated. The
target `resourcePath` location, the location where these resources will live within the package, should never contain
other files. Those resources would inadvertently be available to the public.

To avoid this, the default `resourcePath` location is `/www/${packageName}/`. This location is not likely to conflict
with other resources. However, it is still recommended to verify that the resources are isolated.