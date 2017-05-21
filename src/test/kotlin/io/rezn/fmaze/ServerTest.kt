package io.rezn.fmaze

import org.junit.Test
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals

class ServerTest {

    @Test
    fun t( ) {
        val input = "666|F|60|50\r\n".byteInputStream(Charsets.UTF_8)
        transform(input).forEach { print(it) }
    }


}
