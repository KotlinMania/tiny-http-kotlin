// port-lint: source util/equal_reader.rs
package io.github.kotlinmania.tinyhttp.util

import kotlin.test.Test
import kotlin.test.assertEquals

class EqualReaderTest {
    @Test
    fun testLimit() {
        val reader = EqualReader.new("hello world", 5L)
        assertEquals(5L, reader.size)
        val consumed = reader.consume(5L)
        assertEquals(5L, consumed)
        assertEquals(0L, reader.size)
        assertEquals(0L, reader.consume(5L))
    }

    @Test
    fun testNotEnough() {
        val reader = EqualReader.new("hello world", 5L)
        val first = reader.consume(1L)
        assertEquals(1L, first)
        assertEquals(4L, reader.size)
        val remainder = reader.consume(10L)
        assertEquals(4L, remainder)
        assertEquals(0L, reader.size)
    }
}
