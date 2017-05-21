package io.rezn.fmaze

import java.util.*

/**
 * Created by rlorca on 21.05.17.
 */
class MessageQueue(var expected: Int) {

    // a priority queue with lower sequence number as head
    val queue = PriorityQueue<Command>(Comparator<Command> {a, b -> a.seq - b.seq })

    /*  Adds a command to the queue and
        deques events that match the expected sequence number
        Note: this won't work if the event processing is multi-threaded, as the sequence is lazy
    */
    fun process(command: Command) : Sequence<Command> {

        queue.add(command)

        return generateSequence {
            if (queue.peek()?.seq == expected) {
                expected++
                queue.remove()
            } else {
                null
            }
        }
    }

    fun isEmpty() = queue.isEmpty()

    fun lastSequence() = expected - 1
}
