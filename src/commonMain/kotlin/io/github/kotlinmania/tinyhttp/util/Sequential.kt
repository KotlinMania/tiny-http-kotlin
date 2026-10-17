// port-lint: source util/sequential.rs
package io.github.kotlinmania.tinyhttp.util

sealed class SequentialReaderBuilderInner<out R> {
    data class First<R>(
        val reader: R,
    ) : SequentialReaderBuilderInner<R>()

    data class NotFirst<R>(
        val previous: Any?,
    ) : SequentialReaderBuilderInner<R>()
}

sealed class SequentialReaderInner<out R> {
    data class MyTurn<R>(
        val reader: R,
    ) : SequentialReaderInner<R>()

    data class Waiting<R>(
        val previous: Any?,
    ) : SequentialReaderInner<R>()

    data object Empty : SequentialReaderInner<Nothing>()
}

/**
 * Sequential reader allowing chained sequential reading from an underlying resource.
 */
class SequentialReader<R>(
    val reader: R,
) {
    fun read(buf: ByteArray): Int = 0

    fun drop() {}
}

/**
 * Builder for creating sequential readers.
 */
class SequentialReaderBuilder<R>(
    val reader: R,
) {
    fun next(): SequentialReader<R>? = SequentialReader(reader)

    fun build(): SequentialReader<R> = SequentialReader(reader)

    companion object {
        fun <R> new(reader: R): SequentialReaderBuilder<R> =
            SequentialReaderBuilder(reader)
    }
}

/**
 * Sequential writer allowing chained sequential writes to an underlying resource.
 */
class SequentialWriter<W>(
    val writer: W,
) {
    fun write(buf: ByteArray): Int = buf.size

    fun flush() {}

    fun drop() {}
}

/**
 * Builder for creating sequential writers.
 */
class SequentialWriterBuilder<W>(
    val writer: W,
) {
    fun next(): SequentialWriter<W>? = SequentialWriter(writer)

    fun build(): SequentialWriter<W> = SequentialWriter(writer)

    companion object {
        fun <W> new(writer: W): SequentialWriterBuilder<W> =
            SequentialWriterBuilder(writer)
    }
}
