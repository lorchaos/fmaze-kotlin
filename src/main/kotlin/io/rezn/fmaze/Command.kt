package io.rezn.fmaze

import java.io.InputStream

typealias UserId = String

sealed class Command(open val msg: String, open val seq: Int) {

    data class Broadcast(override val msg: String, override val seq: Int) :  Command(msg, seq)

    data class Unfollow(override val msg: String,  override val seq: Int, val from: UserId, val to: UserId) :  Command(msg, seq)

    data class Follow(override val msg: String,  override val seq: Int, val from: UserId, val to: UserId) :  Command(msg, seq)

    data class PrivateMessage(override val msg: String,  override val seq: Int, val from: UserId, val to: UserId) :  Command(msg, seq)

    data class StatusUpdate(override val msg: String, override val seq: Int, val from: UserId) : Command(msg, seq)


    companion object {

        fun parse(msg: String): Command? {

            val fields = msg.split("|")
            val seqn = fields[0].toInt()
            val type = fields[1]
            val source = fields.elementAtOrNull(2)
            val target = fields.elementAtOrNull(3)

            try {
            // TODO find a better way to handle these nullables
            return when (type) {
                "B" -> Command.Broadcast(msg, seqn)
                "U" -> Command.Unfollow(msg, seqn, source!!, target!!)
                "F" -> Command.Follow(msg, seqn, source!!, target!!)
                "P" -> Command.PrivateMessage(msg, seqn, source!!, target!!)
                "S" -> Command.StatusUpdate(msg, seqn, source!!)
                else -> {
                    println("Unknown message type '$type' : '$msg'")
                    null
                }
            }
            } catch (e: Exception) {

                println("Unable to parse message $msg : ${e.message}")
                return null
            }
        }

        // given an inputstream, generates a sequence of commands
        fun readAll(input: InputStream) : Sequence<Command> = input.bufferedReader(Charsets.UTF_8).lines()
                .map(Command.Companion::parse)
                .iterator()
                .asSequence()
                .filterNotNull()
    }
}