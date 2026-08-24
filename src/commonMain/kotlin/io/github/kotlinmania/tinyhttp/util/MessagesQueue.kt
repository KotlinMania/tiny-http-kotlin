// port-lint: source util/messages_queue.rs
package io.github.kotlinmania.tinyhttp.util

/**
 * Control items stored in the message queue.
 */
sealed class Control<out T> {
    data class Elem<T>(val value: T) : Control<T>()
    data object Unblock : Control<Nothing>()
}

/**
 * Message queue supporting push, unblock signals, and non-blocking pops.
 */
class MessagesQueue<T>(
    capacity: Int = 16,
) {
    private val queue = ArrayDeque<Control<T>>(capacity)

    /**
     * Pushes an element to the back of the queue.
     */
    fun push(value: T) {
        queue.addLast(Control.Elem(value))
    }

    /**
     * Unblocks one receiver waiting in the pop loop.
     */
    fun unblock() {
        queue.addLast(Control.Unblock)
    }

    /**
     * Pops an element from the queue. Returns null when unblock was issued or queue is empty.
     */
    fun pop(): T? = tryPop()

    /**
     * Tries to pop an element without blocking.
     */
    fun tryPop(): T? =
        when (val item = queue.removeFirstOrNull()) {
            is Control.Elem -> item.value
            Control.Unblock -> null
            null -> null
        }

    /**
     * Returns current number of items in the queue.
     */
    fun size(): Int = queue.size

    companion object {
        fun <T> withCapacity(capacity: Int): MessagesQueue<T> =
            MessagesQueue(capacity)
    }
}
