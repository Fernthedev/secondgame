package com.github.fernthedev.packets.player_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import com.github.fernthedev.universal.entity.EntityPlayer
import java.io.Serializable
import java.util.*

@PacketInfo(name = "SEND_TO_SERVER_PLAYER_INFO_PACKET")
class SendToServerPlayerInfoPacket(
    val playerObject: EntityPlayer,
    val entitiesHashCodeMap: Map<UUID, Int>?
) : Packet(), Serializable {

}