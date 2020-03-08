package com.github.fernthedev.packets;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.core.packets.PacketInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@PacketInfo(name = "LEVEL_UP_PACKET")
public class LevelUp extends Packet implements Serializable {
    private final int level;
}
