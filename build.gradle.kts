group = "org.ivcode"
version = "0.0.1-SNAPSHOT"

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    id("org.ivcode.gradle-publish") version "0.1-SNAPSHOT"
}

repositories {
    mavenCentral()
}

java {
    withSourcesJar()
}

dependencies {
    implementation("com.github.spullara.mustache.java:compiler:0.9.14")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        create("gradle-public-resources") {
            id = "org.ivcode.gradle-public-resources"
            implementationClass = "org.ivcode.gradle.resources.ResourcesPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}