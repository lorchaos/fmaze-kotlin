package io.rezn.fmaze

import java.net.Socket

/**
 * Created by rlorca on 21.05.17.
 */
class Client(val id: UserId, val socket: Socket) {

    fun write(command: Command) : Unit {
        socket.getOutputStream().writer(Charsets.UTF_8).let {
            it.write("${command.msg}\r\n")
            it.flush()
        }
    }

    companion object {
        fun create(socket: Socket): Client = socket.getInputStream().bufferedReader(Charsets.UTF_8).let {
            it.readLine().let { Client(it, socket) }
        }
    }
}