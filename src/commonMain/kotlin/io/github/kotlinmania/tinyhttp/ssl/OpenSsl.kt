// port-lint: source ssl/openssl.rs
package io.github.kotlinmania.tinyhttp.ssl

/**
 * OpenSSL context wrapper for TLS server termination.
 */
class OpenSslContext(
    val certificates: ByteArray,
    val privateKey: ByteArray,
) : SslContext {
    override fun isSupported(): Boolean = true

    companion object {
        fun fromPem(certificates: ByteArray, privateKey: ByteArray): OpenSslContext =
            OpenSslContext(certificates, privateKey)
    }
}
