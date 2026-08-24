// port-lint: source util/sequential.rs
package io.github.kotlinmania.tinyhttp.util

/**
 * Sequential reader allowing chained sequential reading from an underlying resource.
 */
class SequentialReader<R>(
    val reader: R,
)

/**
 * Builder for creating sequential readers.
 */
class SequentialReaderBuilder<R>(
    val reader: R,
) {
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
)

/**
 * Builder for creating sequential writers.
 */
class SequentialWriterBuilder<W>(
    val writer: W,
) {
    fun build(): SequentialWriter<W> = SequentialWriter(writer)

    companion object {
        fun <W> new(writer: W): SequentialWriterBuilder<W> =
            SequentialWriterBuilder(writer)
    }
}
