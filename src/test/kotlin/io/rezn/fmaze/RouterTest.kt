package io.rezn.fmaze

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RouterTest {

    private val router = Router()

    // TODO test message delivery

    @Test
    fun followCycle() {

        router.deliver(Command.Follow("", 1, "a", "b"))

        assertTrue(router.followers.get("b")!!.contains("a"));

        router.deliver(Command.Unfollow("", 1, "a", "b"));

        assertTrue(router.followers.get("b")!!.isEmpty())
    }

    @Test
    fun invalidUnfollow() {

        router.deliver(Command.Unfollow("", 1, "z", "zz"));

        assertNull(router.followers.get("zz"))
    }
}