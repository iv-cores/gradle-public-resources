package org.ivcode.gradle.www

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.apply as kApply

class ResourcesExtensionTest {

    @Test
    fun `validate - with valid inputs - sets all properties correctly`() {
        val project: Project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
        }

        extension.validate(project)

        assertEquals("com.example.myproject", extension.packageName)
        assertEquals("PublicResourceConfigurer", extension.className)
        assertEquals("/www/com.example.myproject/", extension.resourcePath)
        assertEquals("/**", extension.url)
        assertEquals("myproject", extension.propertyPrefix)
    }

    @Test
    fun `validate - with blank name - sets package name without separator`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns ""
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            propertyPrefix = "myproject"
        }

        extension.validate(project)
        assertEquals("com.example", extension.packageName)
    }

    @Test
    fun `validate - with blank group - sets package name without separator`() {
        val project = mockk<Project>().kApply {
            every { group } returns ""
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
        }

        extension.validate(project)
        assertEquals("myproject", extension.packageName)
    }

    @Test
    fun `validate - without resources defined - throws exception`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension()

        assertThrows<IllegalArgumentException> {
            extension.validate(project)
        }
    }

    @Test
    fun `validate - with correctly formatted resourcePath`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            resourcePath = "/validPath/"
        }

        extension.validate(project)
        assertEquals("/validPath/", extension.resourcePath)
    }

    @Test
    fun `validate - without prefix & suffix slashes in resourcePath - throws exception`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            resourcePath = "invalidPath" // resourcePath must start and end with '/'
        }

        assertThrows<IllegalArgumentException> {
            extension.validate(project)
        }
    }

    @Test
    fun `validate - with only prefix slash in resourcePath - throws exception`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            resourcePath = "/invalidPath" // resourcePath must start and end with '/'
        }

        assertThrows<IllegalArgumentException> {
            extension.validate(project)
        }
    }

    @Test
    fun `validate - with only suffix slash in resourcePath - throws exception`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            resourcePath = "invalidPath/" // resourcePath must start and end with '/'
        }

        assertThrows<IllegalArgumentException> {
            extension.validate(project)
        }
    }

    @Test
    fun `validate - with valid url - throws exception`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            url = "/validUrl/**" // url must end with '/**'
        }

        extension.validate(project)
        assertEquals("/validUrl/**", extension.url)
    }

    @Test
    fun `validate - with invalid url - throws exception`() {
        val project = mockk<Project>().kApply {
            every { group } returns "com.example"
            every { name } returns "myproject"
        }
        val extension = ResourcesExtension().kApply {
            resources = "src/main/resources"
            url = "/invalidUrl" // url must end with '/**'
        }

        assertThrows<IllegalArgumentException> {
            extension.validate(project)
        }
    }
}