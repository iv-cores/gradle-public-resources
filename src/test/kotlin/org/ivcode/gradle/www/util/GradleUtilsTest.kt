package org.ivcode.gradle.www.util

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class GradleUtilsTest {

    private lateinit var project: Project
    private lateinit var layout: ProjectLayout
    private lateinit var buildDirectory: Directory

    @BeforeEach
    fun setUp() {
        project = mockk()
        layout = mockk()
        buildDirectory = mockk()

        every { project.layout } returns layout

        every { layout.buildDirectory } returns mockk<DirectoryProperty>()

        every { layout.buildDirectory.dir(any<String>()) } returns mockk<Provider<Directory>>()

        every { layout.buildDirectory.dir("generated${File.separator}sources${File.separator}main${File.separator}java") } returns mockk<Provider<Directory>>()

        every { layout.buildDirectory.dir("resources${File.separator}main") } returns mockk<Provider<Directory>>()
    }

    @Test
    fun getGeneratedSourceDirectory_returnsCorrectDirectory() {
        val expectedDirectory = mockk<Directory>()
        every { layout.buildDirectory.dir("generated${File.separator}sources${File.separator}main${File.separator}java").get() } returns expectedDirectory

        val result = project.getGeneratedSourceDirectory()

        assertEquals(expectedDirectory, result)
    }

    @Test
    fun getResourceDirectory_returnsCorrectDirectory() {
        val expectedDirectory = mockk<Directory>()
        every { layout.buildDirectory.dir("resources${File.separator}main").get() } returns expectedDirectory

        val result = project.getResourceDirectory()

        assertEquals(expectedDirectory, result)
    }
}