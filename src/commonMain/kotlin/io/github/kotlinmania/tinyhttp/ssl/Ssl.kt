// port-lint: source ssl.rs
package io.github.kotlinmania.tinyhttp.ssl

/**
 * Common abstraction for SSL / TLS context implementations.
 */
interface SslContext {
    fun isSupported(): Boolean
}

/**
 * Common abstraction for SSL / TLS streams.
 */
interface SslStream {
    fun isSecure(): Boolean = true
}
