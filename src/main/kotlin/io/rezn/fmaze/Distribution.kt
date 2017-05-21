package io.rezn.fmaze

class Distribution(val clients: HashMap<String, Client>) {

    var followers = HashMap<UserId, HashSet<UserId>>()

    fun deliver(command: Command) : Unit {
        println("Delivering $command")
        when (command) {
            is Command.Follow -> follow(command)
            is Command.Unfollow -> unfollow(command)
            is Command.Broadcast -> broadcast(command)
            is Command.StatusUpdate -> update(command)
            is Command.PrivateMessage -> privateMessage(command)
        }
    }

    // all clients get a message
    private fun broadcast(command: Command.Broadcast) =
            clients.values.forEach { it.write(command) }

    // all followers get a message
    private fun update(command: Command.StatusUpdate) =
            followers.get(command.from)?.map(clients::get)?.forEach{ it?.write(command) }

    private fun follow(command: Command.Follow) {

        val set = followers.getOrPut(command.to, { HashSet<UserId>() })

        set.add(command.from)

        println("User ${command.to} has ${set.size} followers")

        // notify folowee
        clients.get(command.to)?.write(command)
    }

    // no notification needed
    private fun unfollow(command: Command.Unfollow) =
            followers.get(command.to)?.remove(command.from)

    // just the 'to' gets a message
    private fun privateMessage(command: Command.PrivateMessage) =
            clients.get(command.to)?.write(command)

}