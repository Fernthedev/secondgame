package com.github.fernthedev.packets.object_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import com.github.fernthedev.universal.entity.NewGsonGameObject

@PacketInfo(name = "ADD_OBJECT_PACKET")
data class AddObjectPacket(
    val newGsonGameObject: NewGsonGameObject
) : Packet() {

}