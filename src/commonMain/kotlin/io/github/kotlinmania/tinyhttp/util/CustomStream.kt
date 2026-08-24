// port-lint: source util/custom_stream.rs
package io.github.kotlinmania.tinyhttp.util

/**
 * A combined stream wrapping a separate reader and writer.
 */
class CustomStream<R, W>(
    val reader: R,
    val writer: W,
) {
    companion object {
        fun <R, W> new(reader: R, writer: W): CustomStream<R, W> =
            CustomStream(reader, writer)
    }
}
