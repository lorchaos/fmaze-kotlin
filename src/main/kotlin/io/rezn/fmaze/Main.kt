package io.rezn.fmaze

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.net.ServerSocket
import java.net.Socket

val EVENT_SERVICE_PORT = 9090
val CLIENT_SERVICE_PORT = 9099
val INITIAL_EVENT_SEQ = 1

fun main(args: Array<String>) {

    val router = Router()

    val clientJob = launch(CommonPool) {
        // creates a client and add to the router
        listen(CLIENT_SERVICE_PORT)
                .map(Client.Companion::create).
                forEach(router::addClient)
    }

    // add each event to the queue, and then routes the messages
    listen(EVENT_SERVICE_PORT)
            // single connection is received
            .take(1)
            .flatMap { Command.readAll(it.getInputStream()) }
            .scan(MessageQueue(INITIAL_EVENT_SEQ)) { c, q -> q.add(c) }
            .flatMap { it.takeAllAvailable() }
            .forEach(router::deliver)

    // stop accepting new clients
    clientJob.cancel()

    router.closeAllClients()

    println("Done! Last event delivered!")
}

// TODO find a way to close the server socket once the sequence is completed
// generates a sequence of sockets from a server socket
fun listen(port: Int): Sequence<Socket> =
        ServerSocket(port).let { generateSequence { it.accept() } }


fun <S, A> Sequence<A>.scan(s: S, f: (a: A, s: S) -> S) = this.map { f(it, s) }