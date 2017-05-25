package io.rezn.fmaze

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by rlorca on 21.05.17.
 */
class MessageQueueTest {

    val queue = MessageQueue(1)

    @Test
    fun queuedMessages() {

        // nothing will be returned, as message is added to the queue
        assertTrue(addToQueue("two", 2).isEmpty())

        // this message is too far ahead, it won't be returned
        assertTrue(addToQueue("ten", 10).isEmpty())

        // and now we get the two messages at once
        val batch = addToQueue("one", 1)

        assertEquals(2, batch.size)
        assertEquals(listOf("one", "two"), batch.map { it.msg })
    }

    @Test
    fun rightOrder() =
            assertEquals(1, addToQueue("one", 1).size)

    private fun addToQueue(msg: String, seq: Int): List<Command> {
        queue.add(Command.Broadcast(msg, seq))
        return queue.takeAllAvailable().toList()
    }
}
