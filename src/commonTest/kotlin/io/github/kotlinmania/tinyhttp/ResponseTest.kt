// port-lint: source response.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResponseTest {
    @Test
    fun testFromString() {
        val response = Response.fromString("hello world")
        assertEquals(StatusCode(200), response.statusCode)
        assertEquals(11L, response.dataLength)
        assertEquals(1, response.headers.size)
        assertTrue(response.headers[0].field.equiv("Content-Type"))
        assertEquals("text/plain; charset=UTF-8", response.headers[0].value)
    }

    @Test
    fun testAddHeaderForbidden() {
        val response = Response.empty(200)
        val initialHeaderCount = response.headers.size

        response.addHeader(Header.fromBytes("Connection", "keep-alive").getOrThrow())
        response.addHeader(Header.fromBytes("Trailer", "Expires").getOrThrow())
        response.addHeader(Header.fromBytes("Transfer-Encoding", "chunked").getOrThrow())
        response.addHeader(Header.fromBytes("Upgrade", "websocket").getOrThrow())

        assertEquals(initialHeaderCount, response.headers.size)
    }

    @Test
    fun testAddHeaderContentTypeOverwrite() {
        val response = Response.empty(200)
        response.addHeader(Header.fromBytes("Content-Type", "text/plain").getOrThrow())
        assertEquals(1, response.headers.size)
        assertEquals("text/plain", response.headers[0].value)

        response.addHeader(Header.fromBytes("Content-Type", "application/json").getOrThrow())
        assertEquals(1, response.headers.size)
        assertEquals("application/json", response.headers[0].value)
    }

    @Test
    fun testChooseTransferEncoding() {
        val identity = chooseTransferEncoding(
            statusCode = StatusCode(200),
            requestHeaders = emptyList(),
            httpVersion = HTTPVersion(1, 1),
            entityLength = 100L,
            chunkedThreshold = 1000L,
        )
        assertEquals(TransferEncoding.Identity, identity)

        val chunked = chooseTransferEncoding(
            statusCode = StatusCode(200),
            requestHeaders = emptyList(),
            httpVersion = HTTPVersion(1, 1),
            entityLength = 5000L,
            chunkedThreshold = 1000L,
        )
        assertEquals(TransferEncoding.Chunked, chunked)

        val noContent = chooseTransferEncoding(
            statusCode = StatusCode(204),
            requestHeaders = emptyList(),
            httpVersion = HTTPVersion(1, 1),
            entityLength = null,
        )
        assertEquals(TransferEncoding.Identity, noContent)
    }
}
