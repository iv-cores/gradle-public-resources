package org.ivcode.gradle.www.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test
import java.io.File

class PackageUtilsTest {

    @Test
    fun `toFilePath - converts string to file path with default separator`() {
        val input = "org.example.package"
        val expected = "org${File.separator}example${File.separator}package"
        assertEquals(expected, input.toFilePath())
    }

    @Test
    fun `toFilePath - converts string to file path with custom separator`() {
        val input = "org.example.package"
        val expected = "org/example/package"
        assertEquals(expected, input.toFilePath("/"))
    }

    @Test
    fun `asPackage - correctly formatted`() {
        val input = "org.example_package2"
        val expected = "org.example_package2"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - converts string to package format`() {
        val input = "Org/Example Package"
        val expected = "org.example_package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - converts string to package format with special characters`() {
        val input = "Org/Example!@#Package"
        val expected = "org.examplepackage"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - converts string to package format with leading and trailing spaces`() {
        val input = "  Org/Example Package  "
        val expected = "org.example_package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - converts string to package format with mixed case`() {
        val input = "Org/Example Package"
        val expected = "org.example_package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - converts string to package with hyphen`() {
        val input = "Org\\Example-Package"
        val expected = "org.example_package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - handles empty string`() {
        val input = ""

        assertThrowsExactly(IllegalArgumentException::class.java) {
            input.asPackage()
        }
    }

    @Test
    fun `asPackage - handles string with only invalid characters`() {
        val input = "!@#$%^&*()"

        assertThrowsExactly(IllegalArgumentException::class.java) {
            input.asPackage()
        }
    }

    @Test
    fun `asPackage - handles string with leading and trailing invalid characters`() {
        val input = "!@#org.example.package!@#"
        val expected = "org.example.package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - handles string with multiple invalid characters`() {
        val input = "org!@#.example!@#.package"
        val expected = "org.example.package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - handles string with multiple spaces and hyphens`() {
        val input = "org  -  example  -  package"
        val expected = "org_example_package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - handles string with multiple underscores`() {
        val input = "org__example__package"
        val expected = "org_example_package"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - handles string with mixed valid and invalid characters`() {
        val input = "org!@#example$%^package"
        val expected = "orgexamplepackage"
        assertEquals(expected, input.asPackage())
    }

    @Test
    fun `asPackage - handles string with leading and trailing dots and underscores`() {
        val input = "._._org.example.package_._."
        val expected = "org.example.package"
        assertEquals(expected, input.asPackage())
    }
}