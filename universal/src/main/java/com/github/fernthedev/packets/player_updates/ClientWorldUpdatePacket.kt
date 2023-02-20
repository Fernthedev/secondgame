package com.github.fernthedev.packets.player_updates

import com.github.fernthedev.lightchat.core.packets.PacketInfo
import com.github.fernthedev.lightchat.core.packets.PacketJSON
import com.github.fernthedev.universal.entity.NewGsonGameObject
import java.io.Serializable
import java.util.*

@PacketInfo(name = "SEND_TO_SERVER_PLAYER_INFO_PACKET")
class ClientWorldUpdatePacket(
    val playerObject: NewGsonGameObject,
    val entitiesHashCodeMap: Map<UUID, Int>?
) : PacketJSON(), Serializable