// port-lint: source util/fused_reader.rs
package io.github.kotlinmania.tinyhttp.util

/**
 * Wraps another reader and provides "fused" behavior.
 * When the underlying reader reaches EOF, it is released
 * and the fused reader becomes empty.
 */
class FusedReader<R>(
    private var inner: R?,
) {
    /**
     * Extracts the inner reader if still present.
     */
    fun intoInner(): R? = inner

    /**
     * Returns true if the inner reader is still available.
     */
    fun hasInner(): Boolean = inner != null

    /**
     * Clears the inner reader upon EOF.
     */
    fun release() {
        inner = null
    }

    companion object {
        fun <R> new(inner: R): FusedReader<R> = FusedReader(inner)
    }
}
