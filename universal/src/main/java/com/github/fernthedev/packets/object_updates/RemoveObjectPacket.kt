package com.github.fernthedev.packets.object_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import java.util.*

@PacketInfo(name = "REMOVE_OBJECT_PACKET")
data class RemoveObjectPacket(
    val uuid: UUID
) : Packet()