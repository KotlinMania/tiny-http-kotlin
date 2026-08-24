// port-lint: source client.rs
package io.github.kotlinmania.tinyhttp

import io.github.kotlinmania.tinyhttp.util.RefinedTcpStream
import io.github.kotlinmania.tinyhttp.util.Stream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ClientTest {
    @Test
    fun testClientConnection() {
        val stream = Stream.Http("raw-conn")
        val (read, write) = RefinedTcpStream.new(stream)
        val client = ClientConnection.new(
            writeSocket = write,
            readSocket = read,
            remoteAddr = "127.0.0.1:9000",
            secure = false,
        )

        assertFalse(client.secure)
        assertEquals("127.0.0.1:9000", client.remoteAddr)
        assertFalse(client.noMoreRequests)
    }
}
