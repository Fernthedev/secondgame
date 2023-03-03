package com.github.fernthedev

import com.github.fernthedev.lightchat.core.PacketJsonRegistry
import com.github.fernthedev.lightchat.core.ProtobufRegistry
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.packets.proto.Packets

object CommonUtil {
    const val MAX_PACKET_IDS = 40
    const val PLAYER_COORD_DIF = 4
    const val PLAYER_VEL_DIF = 1
    
    @JvmStatic
    fun registerNetworking() {
        PacketJsonRegistry.registerPacketPackageFromClass(GameOverPacket::class.java)
        PacketJsonRegistry.registerPacketPackageFromClass(SetCoin::class.java)
        ProtobufRegistry.addMessage(Packets.ClientWorldUpdatePacket.getDefaultInstance())
        ProtobufRegistry.addMessage(Packets.SendObjectsListPacket.getDefaultInstance())
    }
}