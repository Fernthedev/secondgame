package com.github.fernthedev.packets.object_updates;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.core.packets.PacketInfo;
import com.github.fernthedev.universal.GameObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@PacketInfo(name = "SEND_GAME_OBJECT")
@AllArgsConstructor
@Getter
@Deprecated
public class SendGameObject extends Packet implements Serializable {

    private final GameObject gameObject;
}
