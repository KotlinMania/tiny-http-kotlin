// port-lint: source test.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestRequestTest {
    @Test
    fun testBuilder() {
        val request = TestRequest.new()
            .withMethod(Method.Post)
            .withPath("/api/widgets")
            .withBody("42")
            .withHttps()
            .withHeader(Header.parse("X-Custom: value").getOrThrow())

        assertEquals(Method.Post, request.method)
        assertEquals("/api/widgets", request.path)
        assertEquals("42", request.body)
        assertTrue(request.secure)
        assertEquals(1, request.headers.size)
        assertEquals("value", request.headers[0].value)
    }
}
