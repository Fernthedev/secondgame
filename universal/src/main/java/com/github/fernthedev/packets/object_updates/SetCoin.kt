package com.github.fernthedev.packets.object_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import java.io.Serializable


@PacketInfo(name = "SET_COIN")
class SetCoin(
    val coins: Int = 0
) : Packet(), Serializable {
}