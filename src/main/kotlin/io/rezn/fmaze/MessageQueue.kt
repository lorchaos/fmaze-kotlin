package io.rezn.fmaze

import java.util.*

/**
 * Created by rlorca on 21.05.17.
 */
class MessageQueue(var expected: Int) {

    // a priority queue with lower sequence number as head
    val queue = PriorityQueue<Command>(Comparator<Command> { a, b -> a.seq - b.seq })

    fun add(command: Command): MessageQueue {
        queue.add(command)
        return this
    }

    fun takeAllAvailable(): Sequence<Command> = generateSequence {

        if (queue.peek()?.seq == expected) {
            expected++
            queue.remove()
        } else {
            null
        }
    }

    fun isEmpty() = queue.isEmpty()

    fun lastSequence() = expected - 1
}
