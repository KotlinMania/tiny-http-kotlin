// port-lint: source ssl/rustls.rs
package io.github.kotlinmania.tinyhttp.ssl

import io.github.kotlinmania.tinyhttp.Connection
import io.github.kotlinmania.tinyhttp.Shutdown
import io.github.kotlinmania.tinyhttp.SslConfig
import io.github.kotlinmania.zeroize.Zeroizing
import io.github.kotlinmania.tinyhttp.util.Stream as RefinedStream

/**
 * A wrapper around an owned Rustls connection and corresponding stream.
 */
class RustlsStream(
    val connection: Connection,
) {
    fun peerAddr(): String? = connection.peerAddr()

    fun shutdown(how: Shutdown) = connection.shutdown(how)

    fun clone(): RustlsStream = RustlsStream(connection)

    fun read(buf: ByteArray): Int = connection.read(buf)

    fun write(buf: ByteArray): Int = connection.write(buf)

    fun flush() = connection.flush()
}

/**
 * Rustls context wrapper for pure TLS server termination.
 */
class RustlsContext(
    val certificates: ByteArray,
    val privateKey: ByteArray,
) : SslContext {
    override fun isSupported(): Boolean = true

    fun accept(stream: Connection): Result<RustlsStream> =
        Result.success(RustlsStream(stream))

    companion object {
        fun fromPem(
            certificates: ByteArray,
            privateKey: Zeroizing<SslConfig>,
        ): RustlsContext = RustlsContext(certificates, privateKey.value.privateKey)

        fun fromPem(certificates: ByteArray, privateKey: ByteArray): RustlsContext =
            RustlsContext(certificates, privateKey)

        fun from(stream: RustlsStream): RefinedStream =
            RefinedStream.Https(stream)
    }
}
