// port-lint: source ssl/rustls.rs
package io.github.kotlinmania.tinyhttp.ssl

/**
 * Rustls context wrapper for pure TLS server termination.
 */
class RustlsContext(
    val certificates: ByteArray,
    val privateKey: ByteArray,
) : SslContext {
    override fun isSupported(): Boolean = true

    companion object {
        fun fromPem(certificates: ByteArray, privateKey: ByteArray): RustlsContext =
            RustlsContext(certificates, privateKey)
    }
}
