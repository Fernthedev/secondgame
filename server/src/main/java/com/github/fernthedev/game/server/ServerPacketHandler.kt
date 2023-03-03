package com.github.fernthedev.game.server

import com.github.fernthedev.lightchat.core.codecs.AcceptablePacketTypes
import com.github.fernthedev.lightchat.core.packets.PacketProto
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.lightchat.server.api.IPacketHandler
import com.github.fernthedev.packets.proto.Packets


class ServerPacketHandler(
    val server: GameServer
) : IPacketHandler {
    override suspend fun handlePacket(
        acceptablePacketTypes: AcceptablePacketTypes,
        clientConnection: ClientConnection,
        packetId: Int
    ) {
        val proto = acceptablePacketTypes as? PacketProto

        val infoPacket = proto?.message as? Packets.ClientWorldUpdatePacket
        if (infoPacket != null) {
            val packetIdAndTime: Pair<Int, Long> = clientConnection.getPacketId(acceptablePacketTypes.javaClass)
            val id = packetIdAndTime.first
            val time = packetIdAndTime.second

//            if (id -5 < packetId && System.currentTimeMillis() - time > 900)
            server.serverGameHandler.entityHandler.handleClientRespond(clientConnection, infoPacket)
        }
    }
}