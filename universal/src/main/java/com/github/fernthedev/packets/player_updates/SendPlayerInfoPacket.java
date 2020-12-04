package com.github.fernthedev.packets.player_updates;


import com.github.fernthedev.lightchat.core.packets.Packet;
import com.github.fernthedev.lightchat.core.packets.PacketInfo;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@PacketInfo(name = "SEND_PLAYER_INFO_PACKET")
@AllArgsConstructor
@Getter
public class SendPlayerInfoPacket extends Packet implements Serializable {
    private final EntityPlayer playerObject;
}
