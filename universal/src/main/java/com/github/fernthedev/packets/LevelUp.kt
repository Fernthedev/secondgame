package com.github.fernthedev.packets

import com.github.fernthedev.lightchat.core.packets.PacketInfo
import com.github.fernthedev.lightchat.core.packets.PacketJSON
import java.io.Serializable

@PacketInfo(name = "LEVEL_UP_PACKET")
class LevelUp(
    val level: Int = 0
) : PacketJSON(), Serializable