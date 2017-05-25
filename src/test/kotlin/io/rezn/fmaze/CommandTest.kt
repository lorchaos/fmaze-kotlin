package io.rezn.fmaze

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommandTest {

    @Test
    fun follow() {
        val msg = "666|F|60|50"
        assertEquals(Command.Follow(msg, 666, "60", "50"), Command.parse(msg))
    }

    @Test
    fun unfollow() {
        val msg = "666|U|60|50"
        assertEquals(Command.Unfollow(msg, 666, "60", "50"), Command.parse(msg))
    }

    @Test
    fun broadcast() {
        val msg = "666|B|60"
        assertEquals(Command.Broadcast(msg, 666), Command.parse(msg))
    }

    @Test
    fun statusUpdate() {
        val msg = "666|S|60"
        assertEquals(Command.StatusUpdate(msg, 666, "60"), Command.parse(msg))

    }

    @Test
    fun privateMessage() {
        val msg = "666|P|60|50"
        assertEquals(Command.PrivateMessage(msg, 666, "60", "50"), Command.parse(msg))
    }

    @Test
    fun unknownMessage() {
        assertTrue(Command.parse("666|XYZ") == null)
    }

    @Test
    fun commandRead( ) {
        val input = ("777|B\r\n66|F|60|50\r\n").byteInputStream(Charsets.UTF_8)

        assertEquals(listOf(Command.Broadcast("777|B", 777), Command.Follow("66|F|60|50", 66, "60", "50")),
                Command.readAll(input).toList())
    }
}
