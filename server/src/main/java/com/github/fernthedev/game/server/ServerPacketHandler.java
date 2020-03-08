package com.github.fernthedev.game.server;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
import com.github.fernthedev.server.ClientConnection;
import com.github.fernthedev.server.api.IPacketHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServerPacketHandler implements IPacketHandler {

    private final GameServer server;

    @Override
    public void handlePacket(Packet p, ClientConnection clientPlayer) {
        if (p instanceof SendPlayerInfoPacket) {
            SendPlayerInfoPacket infoPacket = (SendPlayerInfoPacket) p;

            // System.out.println("Received player information " + infoPacket.getPlayerObject());

//            NewServerEntityRegistry serverEntityRegistry = server.getServerGameHandler().getEntityHandler();
//
//            EntityPlayer player = infoPacket.getPlayerObject();

//            serverEntityRegistry.updatePlayerObject(clientPlayer, player);

            server.getServerGameHandler().getEntityHandler().updatePlayerObject(clientPlayer, infoPacket.getPlayerObject());

            //UniversalHandler.getThingHandler().updatePlayerObject(infoPacket.getPlayerObject());

            //Server.sendObjectToAllPlayers(infoPacket);
        }
    }
}
