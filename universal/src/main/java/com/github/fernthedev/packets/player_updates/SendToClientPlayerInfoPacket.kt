package com.github.fernthedev.packets.player_updates

import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.lightchat.core.packets.PacketInfo
import com.github.fernthedev.universal.entity.EntityPlayer
import java.io.Serializable

/**
 * Sent by the server
 * to tell the client current information
 * the server has
 *
 */
@PacketInfo(name = "SEND_TO_CLIENT_PLAYER_INFO_PACKET")
class SendToClientPlayerInfoPacket(
    val playerObject: EntityPlayer
) : Packet(), Serializable