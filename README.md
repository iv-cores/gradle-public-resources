[![Build Status](https://jenkins.ivcode.org/job/iv-cores/job/gradle-www/job/main/badge/icon)](https://jenkins.ivcode.org/job/iv-cores/job/gradle-www/job/main/)
[![Reliability Rating](https://sonar.ivcode.org/api/project_badges/measure?project=iv-cores_gradle-www&metric=reliability_rating)](https://sonar.ivcode.org/dashboard?id=iv-cores_gradle-www)
[![Coverage](https://sonar.ivcode.org/api/project_badges/measure?project=iv-cores_gradle-www&metric=coverage)](https://sonar.ivcode.org/dashboard?id=iv-cores_gradle-www)
[![Quality Gate Status](https://sonar.ivcode.org/api/project_badges/measure?project=iv-cores_gradle-www&metric=alert_status)](https://sonar.ivcode.org/dashboard?id=iv-cores_gradle-www)

# gradle-www
This plugin helps create a UI package that serves static resources from a Spring Boot application.

What it does:
- Packages static resources into a JAR file (useful for importing modern UIs, like vue or react, into a Spring Boot application)
- Generates auto-configuration classes to serve the resources, without configuration, from your Spring Boot application

## Usage
The process is to simply build your library (using this plugin) and import it into your Spring Boot application.

### Building Your Library
From your library project, add the plugin to your build script.

#### Plugin Setup
In the `settings.gradle.kts` file, add the plugin repository:
```kotlin
// add the repository
pluginManagement {
    repositories {
        maven { url = uri("https://mvn.ivcode.org/mvn/snapshot") }
    }
}
```

In your `build.gradle.kts` file, apply and configure the plugin:
```kotlin
// apply the plugin
plugins {
    id("org.ivcode.gradle-www") version "<version>"
}

// configure the plugin
www {
    // specify the directory containing your static resources
    resources = "${projectDir}/www"
}

// Tie the plugin tasks to the process that builds your resources (if needed) 
// www-CopyResources is a tasked defined by the plugin. Make this task dependent on the task that builds your resources.
named("www-CopyResources").configure {
    dependsOn("build-resources")  // "build-resources" generically represents a task that builds your resources
}
```

#### Build
To build the package, run the following command:

```shell
./gradlew build
```

### Import Your Library
From your spring-boot application, import your library:

```kotlin
dependencies {
    implementation("com.example:my-ui:1.0.0")
}
```

Spring-boot will see your library and automatically configure the resources to be served from your application.

## Configuration
The plugin is configured using the `www` extension. The following properties are available:

| Property        | Description                                              | Default                    |
|-----------------|----------------------------------------------------------|----------------------------|
| resources       | The directory containing the static resources to package | *required*                 |
| packageName     | The package name for the generated loader class          | `${group}.${name}`         |
| className       | The class name for the generated loader class            | `PublicResourceConfigurer` |
| resourcePath    | The destination within the package for these resources   | `/www/${packageName}/`     |
| url             | The URL path to serve the resources from                 | `/**`                      |
| propertyPrefix  | A prefix to use for the application properties           | `${name}`                  |

<sub>Note: `${group}` and `${name}` refer to the gradle project's group and name properties.</sub>

## Application Properties
Spring application properties (`application.properties`, `application.yml`, etc.) can be used to modify your library at
runtime. Currently, the only configurable property is the `url`.

Assuming the `propertyPrefix` in the `www` configuration is set to `my-ui`, you can override the `url` at runtime with
the following property:

```properties
# application.properties

# Sets the URL path from which your resources will be served.
# The property prefix 'my-ui' is defined in the 'www' configuration using 'propertyPrefix'.
my-ui.url=/ui/**
```

## Tasks
The plugin pulls in the `java-library` plugin and adds the following tasks:

### Generate Sources
 - name: `www-GenerateSources`
 - executes before: `compileJava`

This task generates the classes needed to autoconfigure the application and serve the static resources.

### Copy Resources
 - name: `www-CopyResources`
 - executes before: `processResources`

This task copies the resources from the specified directory to the gradle build directory so that it can be packaged
into the JAR file.

If the resources need to be built or processed in any way before copying, make this task (`www-CopyResources`) dependent
on those build tasks.

## Security Considerations
This plugin could potentially expose sensitive information if public resources are not properly isolated. The
`resourcePath` defined in the `www` configuration specifies the location within the package where your static resources
will reside. To prevent exposing unintended files, ensure that this directory contains only public resources.

By default, the `resourcePath` is set to `/www/${packageName}/`, a location that minimizes conflicts with other
resources. However, it is still recommended to verify that the resources in this path are isolated from other files.