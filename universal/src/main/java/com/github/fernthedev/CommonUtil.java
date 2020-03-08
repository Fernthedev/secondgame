package com.github.fernthedev;

import com.github.fernthedev.core.PacketRegistry;
import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.object_updates.SendGameObject;
import com.github.fernthedev.packets.player_updates.GetCurrentPlayer;

public class CommonUtil {

    public static void registerPackets() {
        PacketRegistry.registerPacketPackageFromClass(GameOverPacket.class);
        PacketRegistry.registerPacketPackageFromClass(SendGameObject.class);
        PacketRegistry.registerPacketPackageFromClass(GetCurrentPlayer.class);
    }

}
