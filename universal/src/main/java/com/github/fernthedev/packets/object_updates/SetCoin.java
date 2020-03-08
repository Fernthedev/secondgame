package com.github.fernthedev.packets.object_updates;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.core.packets.PacketInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@PacketInfo(name = "SET_COIN")
public class SetCoin extends Packet implements Serializable {

    private int coins;

}
