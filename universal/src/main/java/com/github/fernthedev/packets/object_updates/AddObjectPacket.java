package com.github.fernthedev.packets.object_updates;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.core.packets.PacketInfo;
import com.github.fernthedev.universal.entity.NewGsonGameObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@PacketInfo(name = "ADD_OBJECT_PACKET")
@Data
@Getter
@AllArgsConstructor
public class AddObjectPacket extends Packet {

    private NewGsonGameObject newGsonGameObject;

}
