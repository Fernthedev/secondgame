package com.github.fernthedev.game.server

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.lightchat.server.api.IPacketHandler
import com.github.fernthedev.packets.player_updates.ClientWorldUpdatePacket
import org.apache.commons.lang3.tuple.Pair


class ServerPacketHandler(
    val server: GameServer
) : IPacketHandler {
    override fun handlePacket(p: Packet, clientPlayer: ClientConnection, packetId: Int) {
        if (p is ClientWorldUpdatePacket) {
            val infoPacket: ClientWorldUpdatePacket = p
            val packetIdAndTime: Pair<Int, Long> = clientPlayer.getPacketId(p.javaClass)
            val id = packetIdAndTime.left
            val time = packetIdAndTime.right

//            if (id -5 < packetId && System.currentTimeMillis() - time > 900)
            server.serverGameHandler.entityHandler.handleClientRespond(clientPlayer, infoPacket)
        }
    }
}