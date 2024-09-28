group = "org.ivcode"
version = "0.1.0-SNAPSHOT"

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    id("org.ivcode.gradle-info") version "0.1-SNAPSHOT"
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
    testImplementation("io.mockk:mockk:1.13.12")
}

gradlePlugin {
    plugins {
        create("gradle-www") {
            id = "org.ivcode.gradle-www"
            implementationClass = "org.ivcode.gradle.www.ResourcesPlugin"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
