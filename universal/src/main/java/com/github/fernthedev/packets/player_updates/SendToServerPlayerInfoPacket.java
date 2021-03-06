package com.github.fernthedev.packets.player_updates;


import com.github.fernthedev.lightchat.core.packets.Packet;
import com.github.fernthedev.lightchat.core.packets.PacketInfo;
import com.github.fernthedev.universal.entity.EntityPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@PacketInfo(name = "SEND_TO_SERVER_PLAYER_INFO_PACKET")
@AllArgsConstructor
@Getter
public class SendToServerPlayerInfoPacket extends Packet implements Serializable {
    private final EntityPlayer playerObject;
    private final Map<UUID, Integer> entitiesHashCodeMap;
}
