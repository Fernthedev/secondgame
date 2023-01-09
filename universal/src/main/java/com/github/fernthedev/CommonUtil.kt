package com.github.fernthedev

import com.github.fernthedev.lightchat.core.PacketRegistry
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.packets.object_updates.SendObjectsList
import com.github.fernthedev.packets.player_updates.ClientWorldUpdatePacket

object CommonUtil {
    const val MAX_PACKET_IDS = 40
    const val PLAYER_COORD_DIF = 4
    const val PLAYER_VEL_DIF = 1
    
    @JvmStatic
    fun registerNetworking() {
        PacketRegistry.registerPacketPackageFromClass(GameOverPacket::class.java)
        PacketRegistry.registerPacketPackageFromClass(SendObjectsList::class.java)
        PacketRegistry.registerPacketPackageFromClass(ClientWorldUpdatePacket::class.java)
    }
}