package io.rezn.fmaze

class Router() {

    val clients = HashMap<UserId, Client>()
    val followers = HashMap<UserId, HashSet<UserId>>()

    fun addClient(client: Client) {
        clients.put(client.id, client);
    }

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
            clients.values.forEach { it.write(command.msg) }

    // all followers get a message
    private fun update(command: Command.StatusUpdate) =
            followers.get(command.from)?.map(clients::get)?.forEach{ it?.write(command.msg) }

    private fun follow(command: Command.Follow) {

        followers.getOrPut(command.to, { HashSet<UserId>() }).add(command.from)

        // notify followee
        clients.get(command.to)?.write(command.msg)
    }

    // no notification needed
    private fun unfollow(command: Command.Unfollow) =
            followers.get(command.to)?.remove(command.from)

    // just the 'to' gets a message
    private fun privateMessage(command: Command.PrivateMessage) =
            clients.get(command.to)?.write(command.msg)

    fun closeAllClients() {
        clients.values.forEach{ it.close() }
        clients.clear()
    }
}