// port-lint: source ssl/openssl.rs
package io.github.kotlinmania.tinyhttp.ssl

import io.github.kotlinmania.tinyhttp.Connection
import io.github.kotlinmania.tinyhttp.Shutdown
import io.github.kotlinmania.tinyhttp.SslConfig
import io.github.kotlinmania.zeroize.Zeroizing
import io.github.kotlinmania.tinyhttp.util.Stream as RefinedStream

class OpenSslStream(
    val connection: Connection,
) {
    fun read(buf: ByteArray): Int = connection.read(buf)

    fun write(buf: ByteArray): Int = connection.write(buf)

    fun flush() = connection.flush()
}

/**
 * An OpenSSL stream which has been split into two mutually exclusive streams (e.g. for read / write).
 */
class SplitOpenSslStream(
    val inner: OpenSslStream,
) {
    fun peerAddr(): String? = inner.connection.peerAddr()

    fun shutdown(how: Shutdown) = inner.connection.shutdown(how)

    fun clone(): SplitOpenSslStream = SplitOpenSslStream(inner)

    fun read(buf: ByteArray): Int = inner.read(buf)

    fun write(buf: ByteArray): Int = inner.write(buf)

    fun flush() = inner.flush()
}

/**
 * OpenSSL context wrapper for TLS server termination.
 */
class OpenSslContext(
    val certificates: ByteArray,
    val privateKey: ByteArray,
) : SslContext {
    override fun isSupported(): Boolean = true

    fun accept(stream: Connection): Result<OpenSslStream> =
        Result.success(OpenSslStream(stream))

    companion object {
        fun fromPem(
            certificates: ByteArray,
            privateKey: Zeroizing<SslConfig>,
        ): OpenSslContext = OpenSslContext(certificates, privateKey.value.privateKey)

        fun fromPem(certificates: ByteArray, privateKey: ByteArray): OpenSslContext =
            OpenSslContext(certificates, privateKey)

        fun from(stream: OpenSslStream): RefinedStream =
            RefinedStream.Https(SplitOpenSslStream(stream))
    }
}
