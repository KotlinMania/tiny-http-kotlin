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

    fun read(buf: ByteArray): Int {
        if (size == 0L) return 0
        val toRead = minOf(buf.size.toLong(), size).toInt()
        size -= toRead
        return toRead
    }

    fun drop() {
        var remainingToRead = size
        while (remainingToRead > 0) {
            val count = minOf(remainingToRead, 1024L)
            remainingToRead -= count
        }
        size = 0L
    }

    companion object {
        fun <R> new(reader: R, size: Long): EqualReader<R> =
            EqualReader(reader, size)
    }
}
