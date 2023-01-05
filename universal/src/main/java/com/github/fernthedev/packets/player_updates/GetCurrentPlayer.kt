package com.github.fernthedev.packets.player_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import java.io.Serializable

@PacketInfo(name = "GET_CURRENT_PLAYER")
class GetCurrentPlayer : Packet(), Serializable