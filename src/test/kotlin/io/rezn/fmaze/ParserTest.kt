package io.rezn.fmaze

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParserTest {

    @Test
    fun follow() {
        val msg = "666|F|60|50"
        assertEquals(Command.Follow(msg, 666, "60", "50"), Parser.parse(msg).right())
    }

    @Test
    fun unfollow() {
        val msg = "666|U|60|50"
        assertEquals(Command.Unfollow(msg, 666, "60", "50"), Parser.parse(msg).right())
    }

    @Test
    fun broadcast() {
        val msg = "666|B|60"
        assertEquals(Command.Broadcast(msg, 666), Parser.parse(msg).right())
    }

    @Test
    fun statusUpdate() {
        val msg = "666|S|60"
        assertEquals(Command.StatusUpdate(msg, 666, "60"), Parser.parse(msg).right())

    }

    @Test
    fun privateMessage() {
        val msg = "666|P|60|50"
        assertEquals(Command.PrivateMessage(msg, 666, "60", "50"), Parser.parse(msg).right())
    }

    @Test
    fun unknownMessage() {
        assertTrue(Parser.parse("666|XYZ").left() is Error)
    }
}
