package com.github.fernthedev.game.server

import com.github.fernthedev.lightchat.core.packets.PacketJSON
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.lightchat.server.api.IPacketHandler
import com.github.fernthedev.packets.player_updates.ClientWorldUpdatePacket


class ServerPacketHandler(
    val server: GameServer
) : IPacketHandler {
    override fun handlePacket(p: PacketJSON, clientConnection: ClientConnection, packetId: Int) {
        if (p is ClientWorldUpdatePacket) {
            val infoPacket: ClientWorldUpdatePacket = p
            val packetIdAndTime: Pair<Int, Long> = clientConnection.getPacketId(p.javaClass)
            val id = packetIdAndTime.first
            val time = packetIdAndTime.second

//            if (id -5 < packetId && System.currentTimeMillis() - time > 900)
            server.serverGameHandler.entityHandler.handleClientRespond(clientConnection, infoPacket)
        }
    }
}