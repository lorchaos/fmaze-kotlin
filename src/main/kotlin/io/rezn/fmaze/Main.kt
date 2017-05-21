package io.rezn.fmaze

import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CompletableFuture

val EVENT_SERVICE_PORT = 9090
val CLIENT_SERVICE_PORT = 9099
val INITIAL_EVENT_SEQ = 1

fun main(args: Array<String>) {

    val queue = MessageQueue(INITIAL_EVENT_SEQ)
    val router = Router()

    val clientFuture = CompletableFuture.runAsync {

        // creates a client and add to the router
        listen(CLIENT_SERVICE_PORT)
                .map(Client.Companion::create)
                .forEach(router::addClient)
    }

    // add each event to the queue, and then routes the messages
    listen(EVENT_SERVICE_PORT)
            .take(1) // single connection is received
            .flatMap { Command.read(it.getInputStream()) }
            .flatMap(queue::process)
            .forEach(router::deliver)

    // stop accepting new clients
    clientFuture.cancel(true)

    router.closeAllClients()


    println("Done! Last event delivered: ${queue.lastSequence()} empty: ${queue.isEmpty()}")
}

// TODO find a way to close the server socket once the sequence is completed
// generates a sequence of sockets from a server socket
fun listen(port: Int) : Sequence<Socket> =
        ServerSocket(port).let { generateSequence { it.accept() } }