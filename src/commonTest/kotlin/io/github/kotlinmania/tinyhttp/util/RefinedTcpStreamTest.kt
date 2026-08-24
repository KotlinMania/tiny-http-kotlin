// port-lint: source util/refined_tcp_stream.rs
package io.github.kotlinmania.tinyhttp.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RefinedTcpStreamTest {
    @Test
    fun testRefinedTcpStreamSplit() {
        val stream = Stream.Http("conn-placeholder")
        val (read, write) = RefinedTcpStream.new(stream)
        assertTrue(read.closeRead)
        assertFalse(read.closeWrite)
        assertFalse(write.closeRead)
        assertTrue(write.closeWrite)
        assertFalse(read.isSecure())
    }

    @Test
    fun testSecureStream() {
        val stream = Stream.Https("ssl-placeholder")
        assertTrue(stream.isSecure())
    }
}
