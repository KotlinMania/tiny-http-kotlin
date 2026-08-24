// port-lint: source ssl.rs
package io.github.kotlinmania.tinyhttp.ssl

import kotlin.test.Test
import kotlin.test.assertTrue

class SslTest {
    @Test
    fun testSslContexts() {
        val openSsl = OpenSslContext.fromPem(byteArrayOf(1, 2), byteArrayOf(3, 4))
        assertTrue(openSsl.isSupported())

        val rustls = RustlsContext.fromPem(byteArrayOf(5, 6), byteArrayOf(7, 8))
        assertTrue(rustls.isSupported())
    }
}
