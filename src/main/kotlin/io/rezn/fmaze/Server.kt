package io.rezn.fmaze

import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.stream.Stream
import kotlin.collections.HashMap

val EVENT_SERVICE_PORT = 9090
val CLIENT_SERVICE_PORT = 9099

fun main(args: Array<String>) {

    val clients = HashMap<String, Client>()
    val distribution = Distribution(clients)

    listen(CLIENT_SERVICE_PORT) {
        val client = Client.create(it)
        clients.put(client.id, client)
    }

    listen(EVENT_SERVICE_PORT) {
        transform(it.getInputStream()).forEach { process(it, distribution::deliver) }
    }

    Thread.sleep(10_000);
}


fun listen(port: Int, block: (Socket) -> Unit) {

    Thread{
        ServerSocket(port).use {
            while(true) {
                block(it.accept())
            }
        }
        println("End socket of $port")
    }.start()

}


fun transform(input: InputStream) : Stream<Either<Error, Command>> =
    input.bufferedReader(Charsets.UTF_8).lines().map(Parser::parse)


var comparator = Comparator<Command> {a: Command, b: Command -> a.seq - b.seq }

var expected = 1

var queue = PriorityQueue<Command>(comparator)

fun process(command: Either<Error, Command>, f: (Command) -> Unit) {

    command.right {
        queue.add(it)

        while(!queue.isEmpty() && queue.peek().seq == expected) {
            f(queue.remove())
            expected++
        }
    }
}
