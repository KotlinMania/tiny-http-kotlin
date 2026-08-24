// port-lint: source util/equal_reader.rs
package io.github.kotlinmania.tinyhttp.util

/**
 * A reader that tracks and limits reading up to an exact number of bytes from a sub-reader.
 */
class EqualReader<R>(
    val reader: R,
    var size: Long,
) {
    /**
     * Consumes up to [count] bytes from the remaining capacity.
     */
    fun consume(count: Long): Long {
        if (size == 0L) return 0L
        val consumed = minOf(size, count)
        size -= consumed
        return consumed
    }

    companion object {
        fun <R> new(reader: R, size: Long): EqualReader<R> =
            EqualReader(reader, size)
    }
}
