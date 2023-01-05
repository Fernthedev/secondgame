package com.github.fernthedev.game.server;


import com.github.fernthedev.lightchat.core.packets.Packet;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.lightchat.server.api.IPacketHandler;
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

@RequiredArgsConstructor
public class ServerPacketHandler implements IPacketHandler {

    private final GameServer server;

    @Override
    public void handlePacket(Packet p, ClientConnection clientPlayer, int packetId) {
        if (p instanceof SendToServerPlayerInfoPacket) {
            SendToServerPlayerInfoPacket infoPacket = (SendToServerPlayerInfoPacket) p;

            Pair<Integer, Long> packetIdAndTime = clientPlayer.getPacketId(p.getClass());
            Integer id = packetIdAndTime.getLeft();
            Long time = packetIdAndTime.getRight();

//            if (id -5 < packetId && System.currentTimeMillis() - time > 900)
            server.getServerGameHandler().getEntityHandler().handleClientRespond(clientPlayer, infoPacket);
        }
    }
}
