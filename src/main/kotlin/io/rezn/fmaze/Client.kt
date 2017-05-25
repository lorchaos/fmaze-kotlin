package io.rezn.fmaze

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.Closeable
import java.io.OutputStreamWriter
import java.net.Socket

class Client(val id: UserId, val socket: Socket) : Closeable {

    val out : OutputStreamWriter = socket.getOutputStream().writer(Charsets.UTF_8)

    init {
        println("Client created $id")
    }

    fun write(msg: String) : Unit {

        //println("Client $id receiving $msg")
        out.let {
            it.write("${msg}\r\n")
            it.flush()
        }
    }

    override fun close() = socket.close()

    companion object {

        // Creates a client from its ID and Socket
        fun create(socket: Socket): Client = socket.getInputStream().bufferedReader(Charsets.UTF_8).readLine().let {
            Client(it, socket)
        }
    }
}