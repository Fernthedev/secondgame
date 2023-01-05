package com.github.fernthedev.packets.object_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import com.github.fernthedev.universal.entity.EntityPlayer
import com.github.fernthedev.universal.entity.NewGsonGameObject
import java.io.Serializable
import java.util.*

@PacketInfo(name = "SEND_OBJECT_LIST")
class SendObjectsList(
    val objectList: Map<UUID, NewGsonGameObject>,
    val mainPlayer: EntityPlayer
) : Packet(), Serializable