// port-lint: source util/sequential.rs
package io.github.kotlinmania.tinyhttp.util

import kotlin.test.Test
import kotlin.test.assertEquals

class SequentialTest {
    @Test
    fun testSequentialReader() {
        val builder = SequentialReaderBuilder.new("payload")
        val reader = builder.build()
        assertEquals("payload", reader.reader)
    }

    @Test
    fun testSequentialWriter() {
        val builder = SequentialWriterBuilder.new(StringBuilder())
        val writer = builder.build()
        writer.writer.append("content")
        assertEquals("content", writer.writer.toString())
    }
}
