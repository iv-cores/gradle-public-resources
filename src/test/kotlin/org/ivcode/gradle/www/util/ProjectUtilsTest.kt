package org.ivcode.gradle.www.util

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import test.utils.mock
import java.io.File

class ProjectUtilsTest {

    private lateinit var project: Project
    private lateinit var layout: ProjectLayout
    private lateinit var buildDirectory: Directory

    @BeforeEach
    fun setUp() {
        project = mock<Project>()
        layout = mock<ProjectLayout>()
        buildDirectory = mock<Directory>()

        `when`(project.layout).thenReturn(layout)

        `when`(layout.buildDirectory)
            .thenReturn(mock(DirectoryProperty::class.java))

        `when`(layout.buildDirectory.dir(anyString()))
            .thenReturn(mock<Provider<Directory>>())

        `when`(layout.buildDirectory.dir("generated${File.separator}sources${File.separator}main${File.separator}java"))
            .thenReturn(mock<Provider<Directory>>())

        `when`(layout.buildDirectory.dir("resources${File.separator}main"))
            .thenReturn(mock<Provider<Directory>>())
    }

    @Test
    fun getGeneratedSourceDirectory_returnsCorrectDirectory() {
        val expectedDirectory = mock<Directory>()
        `when`(layout.buildDirectory.dir("generated${File.separator}sources${File.separator}main${File.separator}java").get())
            .thenReturn(expectedDirectory)

        val result = project.getGeneratedSourceDirectory()

        assertEquals(expectedDirectory, result)
    }

    @Test
    fun getResourceDirectory_returnsCorrectDirectory() {
        val expectedDirectory = mock<Directory>()
        `when`(layout.buildDirectory.dir("resources${File.separator}main").get())
            .thenReturn(expectedDirectory)

        val result = project.getResourceDirectory()

        assertEquals(expectedDirectory, result)
    }
}
