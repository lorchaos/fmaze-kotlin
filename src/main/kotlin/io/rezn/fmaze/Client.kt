package io.rezn.fmaze

import java.io.OutputStreamWriter
import java.net.Socket

class Client(val id: UserId, val socket: Socket) {

    val out : OutputStreamWriter = socket.getOutputStream().writer(Charsets.UTF_8)

    fun write(msg: String) : Unit {
        out.let {
            it.write("${msg}\r\n")
            it.flush()
        }
    }

    fun close() = socket.close()

    companion object {

        // Creates a client from a Socket
        fun create(socket: Socket): Client = socket.getInputStream().bufferedReader(Charsets.UTF_8).let {
            it.readLine().let { Client(it, socket) }
        }
    }
}