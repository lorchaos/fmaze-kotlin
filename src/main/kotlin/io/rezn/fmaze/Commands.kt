package io.rezn.fmaze

/**
 * Created by rlorca on 20.05.17.
 */

typealias UserId = String


sealed class Command(open val msg: String, open val seq: Int) {

    data class Broadcast(override val msg: String, override val seq: Int) :  Command(msg, seq)

    data class Unfollow(override val msg: String,  override val seq: Int, val from: UserId, val to: UserId) :  Command(msg, seq)

    data class Follow(override val msg: String,  override val seq: Int, val from: UserId, val to: UserId) :  Command(msg, seq)

    data class PrivateMessage(override val msg: String,  override val seq: Int, val from: UserId, val to: UserId) :  Command(msg, seq)

    data class StatusUpdate(override val msg: String, override val seq: Int, val from: UserId) : Command(msg, seq)
}

object Parser {

    fun parse(msg: String) : Either<Error, Command>  {

        val fields = msg.split("|")
        val seq = fields[0].toInt()

        return when(fields[1]) {
            "B" -> Either.value(Command.Broadcast(msg, seq))
            "U" -> Either.value(Command.Unfollow(msg, seq, fields[2], fields[3]))
            "F" -> Either.value(Command.Follow(msg, seq, fields[2], fields[3]))
            "P" -> Either.value(Command.PrivateMessage(msg, seq, fields[2], fields[3]))
            "S" -> Either.value(Command.StatusUpdate(msg, seq, fields[2]))
            else -> Either.fail(Error("Invalid message type: '${fields[1]}'"))
        }
    }
}