package org.ivcode.gradle.www.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrowsExactly
import org.junit.jupiter.api.Test

class StringUtilsTest {

    @Test
    fun `requireNotNullOrBlank - with non-blank string - returns string`() {
        val input = "valid string"
        val result = input.requireNotNullOrBlank()
        assertEquals(input, result)
    }

    @Test
    fun `requireNotNullOrBlank - with blank string throws exception`() {
        val input = "   "
        assertThrowsExactly(IllegalArgumentException::class.java) {
            input.requireNotNullOrBlank()
        }
    }

    @Test
    fun `requireNotNullOrBlank - withNullString - throws exception`() {
        val input: String? = null
        assertThrowsExactly(IllegalArgumentException::class.java) {
            input.requireNotNullOrBlank()
        }
    }

    @Test
    fun `ifNullOrBlank - with non-blank string - returns string`() {
        val input = "valid string"
        val result = input.ifNullOrBlank("default")
        assertEquals(input, result)
    }

    @Test
    fun `ifNullOrBlank - with blank string - returns default`() {
        val input = "   "
        val result = input.ifNullOrBlank("default")
        assertEquals("default", result)
    }

    @Test
    fun `ifNullOrBlank - with null string - returns default`() {
        val input: String? = null
        val result = input.ifNullOrBlank("default")
        assertEquals("default", result)
    }

    @Test
    fun `ifNullOrBlank - with non-blank string and block - returns string`() {
        val input = "valid string"
        val result = input.ifNullOrBlank { "default" }
        assertEquals(input, result)
    }

    @Test
    fun `ifNullOrBlank - with blank string and block - returns block result`() {
        val input = "   "
        val result = input.ifNullOrBlank { "default" }
        assertEquals("default", result)
    }

    @Test
    fun `ifNullOrBlank - with null string and block - returns block result`() {
        val input: String? = null
        val result = input.ifNullOrBlank { "default" }
        assertEquals("default", result)
    }
}