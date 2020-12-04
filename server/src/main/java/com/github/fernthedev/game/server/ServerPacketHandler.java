package com.github.fernthedev.game.server;


import com.github.fernthedev.lightchat.core.packets.Packet;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.lightchat.server.api.IPacketHandler;
import com.github.fernthedev.packets.player_updates.SendPlayerInfoPacket;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServerPacketHandler implements IPacketHandler {

    private final GameServer server;

    @Override
    public void handlePacket(Packet p, ClientConnection clientPlayer, int packetId) {
        if (p instanceof SendPlayerInfoPacket) {
            SendPlayerInfoPacket infoPacket = (SendPlayerInfoPacket) p;

            // System.out.println("Received player information " + infoPacket.getPlayerObject());

//            NewServerEntityRegistry serverEntityRegistry = server.getServerGameHandler().getEntityHandler();
//
//            EntityPlayer player = infoPacket.getPlayerObject();

//            serverEntityRegistry.updatePlayerObject(clientPlayer, player);

            if (clientPlayer.getPacketId(p.getClass()).getLeft() -5 < packetId && System.currentTimeMillis() - clientPlayer.getPacketId(p.getClass()).getRight() > 900)
                server.getServerGameHandler().getEntityHandler().handleClientRespond(clientPlayer, infoPacket);

            //UniversalHandler.getThingHandler().updatePlayerObject(infoPacket.getPlayerObject());

            //Server.sendObjectToAllPlayers(infoPacket);
        }
    }
}
