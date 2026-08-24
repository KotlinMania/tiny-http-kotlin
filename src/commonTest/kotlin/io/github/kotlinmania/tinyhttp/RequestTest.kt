// port-lint: source request.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RequestTest {
    @Test
    fun testRequestCreation() {
        val req = Request.newRequest(
            secure = false,
            method = Method.Get,
            path = "/index.html",
            version = HTTPVersion(1, 1),
            headers = listOf(Header.fromBytes("Host", "localhost").getOrThrow()),
            remoteAddr = "127.0.0.1:12345",
            body = "Hello".encodeToByteArray(),
        ).getOrThrow()

        assertFalse(req.secure)
        assertEquals(Method.Get, req.method)
        assertEquals("/index.html", req.url())
        assertEquals(HTTPVersion(1, 1), req.httpVersion)
        assertEquals("127.0.0.1:12345", req.remoteAddr)
        assertEquals("Hello", req.asString())
        assertEquals(5L, req.bodyLength)
    }

    @Test
    fun testExpectationFailed() {
        val res = Request.newRequest(
            secure = false,
            method = Method.Post,
            path = "/upload",
            version = HTTPVersion(1, 1),
            headers = listOf(Header.fromBytes("Expect", "unknown-expectation").getOrThrow()),
            body = ByteArray(0),
        )

        assertTrue(res.isFailure)
    }
}
