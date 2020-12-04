package com.github.fernthedev;


import com.github.fernthedev.lightchat.core.PacketRegistry;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.packets.GameOverPacket;
import com.github.fernthedev.packets.object_updates.SendGameObject;
import com.github.fernthedev.packets.player_updates.GetCurrentPlayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    public static final int MAX_PACKET_IDS = 40;
    public static final int PLAYER_COORD_DIF = 4;
    public static final int PLAYER_VEL_DIF = 1;

    public static void registerNetworking() {
        StaticHandler.setLineLimit(4 * 1000000);

        PacketRegistry.registerPacketPackageFromClass(GameOverPacket.class);
        PacketRegistry.registerPacketPackageFromClass(SendGameObject.class);
        PacketRegistry.registerPacketPackageFromClass(GetCurrentPlayer.class);
    }

}
