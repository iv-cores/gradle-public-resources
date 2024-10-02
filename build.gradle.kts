group = "org.ivcode"
version = "0.1.0-SNAPSHOT"

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    jacoco
    id("org.sonarqube") version "5.1.0.4882"
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

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
tasks.build { dependsOn("jacocoTestReport") }

sonarqube {
    properties {
        property("sonar.projectKey", name)
        property("sonar.host.url", System.getenv("SONAR_HOST_URL") ?: throw IllegalStateException("SONAR_HOST_URL is not set"))
        property("sonar.login", System.getenv("SONAR_LOGIN") ?: throw IllegalStateException("SONAR_LOGIN is not set"))
        property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml")

        System.getProperty("sonar.scm.revision")?.let {
            property("sonar.scm.revision", it)
        }
        System.getProperty("sonar.links.scm")?.let {
            property("sonar.links.scm", it)
        }
        System.getProperty("sonar.links.ci")?.let {
            property("sonar.links.ci", it)
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
