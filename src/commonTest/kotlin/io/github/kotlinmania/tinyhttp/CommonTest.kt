// port-lint: source common.rs
package io.github.kotlinmania.tinyhttp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommonTest {
    @Test
    fun testParseHeader() {
        val header = Header.parse("Content-Type: text/html").getOrThrow()

        assertTrue(header.field.equiv("content-type"))
        assertEquals("text/html", header.value)

        assertTrue(Header.parse("hello world").isFailure)
    }

    @Test
    fun formatsDateCorrectly() {
        val httpDate = formatHttpDate(420895020L)
        assertEquals("Wed, 04 May 1983 11:17:00 GMT", httpDate)
    }

    @Test
    fun testParseHeaderWithDoublecolon() {
        val header = Header.parse("Time: 20: 34").getOrThrow()

        assertTrue(header.field.equiv("time"))
        assertEquals("20: 34", header.value)
    }

    // This tests resistance to RUSTSEC-2020-0031: "HTTP Request smuggling
    // through malformed Transfer Encoding headers"
    // (https://rustsec.org/advisories/RUSTSEC-2020-0031.html).
    @Test
    fun testStrictHeaders() {
        assertTrue(Header.parse("Transfer-Encoding : chunked").isFailure)
        assertTrue(Header.parse(" Transfer-Encoding: chunked").isFailure)
        assertTrue(Header.parse("Transfer Encoding: chunked").isFailure)
        assertTrue(Header.parse(" Transfer\tEncoding : chunked").isFailure)
        assertTrue(Header.parse("Transfer-Encoding: chunked").isSuccess)
        assertTrue(Header.parse("Transfer-Encoding: chunked ").isSuccess)
        assertTrue(Header.parse("Transfer-Encoding:   chunked ").isSuccess)
    }
}
