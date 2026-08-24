// port-lint: source util/mod.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilTest {
    @Test
    fun testParseHeader() {
        val result = parseHeaderValue("text/html, text/plain; q=1.5 , image/png ; q=2.0")

        assertEquals(3, result.size)
        assertEquals("text/html", result[0].first)
        assertEquals(1.0f, result[0].second)
        assertEquals("text/plain", result[1].first)
        assertEquals(1.5f, result[1].second)
        assertEquals("image/png", result[2].first)
        assertEquals(2.0f, result[2].second)
    }
}
