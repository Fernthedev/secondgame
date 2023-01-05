package com.github.fernthedev.packets.object_updates;

import com.github.fernthedev.lightchat.core.packets.Packet;
import com.github.fernthedev.lightchat.core.packets.PacketInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@PacketInfo(name = "REMOVE_OBJECT_PACKET")
@Data
@Getter
@AllArgsConstructor
public class RemoveObjectPacket extends Packet {

    private UUID uuid;

}
