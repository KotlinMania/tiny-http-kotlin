// port-lint: source lib.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ServerTest {
    @Test
    fun testServerCreationAndRequests() {
        val server = Server.http("127.0.0.1", 8080).getOrThrow()
        assertFalse(server.isClosed)
        assertEquals("127.0.0.1:8080", server.listeningAddr.toString())

        val req = Request.newRequest(
            secure = false,
            method = Method.Get,
            path = "/health",
            version = HTTPVersion(1, 1),
            headers = emptyList(),
        ).getOrThrow()

        server.enqueueRequest(req)
        val popped = server.tryRecv()
        assertEquals("/health", popped?.url())
        assertEquals(Method.Get, popped?.method)

        server.unblock()
        assertTrue(server.isClosed)
        assertNull(server.tryRecv())
    }
}
