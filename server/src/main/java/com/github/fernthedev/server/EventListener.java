package com.github.fernthedev.server;

import com.github.fernthedev.packets.PlayerUpdates.SendPlayerInfoPacket;
import com.github.fernthedev.packets.PlayerUpdates.SendToGetInfo;
import com.github.fernthedev.packets.PongPacket;
import com.github.fernthedev.server.gameHandler.EntityHandler;
import com.github.fernthedev.server.gameHandler.ServerGameHandler;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;

import java.util.concurrent.TimeUnit;
public class EventListener {

    private ClientPlayer clientPlayer;

    public EventListener(ClientPlayer clientPlayer) {
     this.clientPlayer = clientPlayer;
    }

    public void recieved(Object p) {

        System.out.println(p + " is the packet");

        if(p instanceof PongPacket) {
            PongPacket packet = (PongPacket) p;
            long time = (System.nanoTime() - packet.getTime() ) / 1000000;

            System.out.println("Ping: " + TimeUnit.NANOSECONDS.toMillis(time) + " ms");
        } else if (p instanceof SendPlayerInfoPacket) {
            SendPlayerInfoPacket infoPacket = (SendPlayerInfoPacket) p;

           // System.out.println("Received player information " + infoPacket.getPlayerObject());

            ServerGameHandler.getEntityHandler().updatePlayerObject(clientPlayer,infoPacket.getPlayerObject());

            //UniversalHandler.getThingHandler().updatePlayerObject(infoPacket.getPlayerObject());

            //Server.sendObjectToAllPlayers(infoPacket);
        } else if (p instanceof SendToGetInfo) {
            SendToGetInfo info = (SendToGetInfo) p;

            UniversalPlayer player = new UniversalPlayer(info.getKeepPlayer(), EntityHandler.playerClientMap.get(clientPlayer));

            UniversalHandler.getThingHandler().updatePlayerObject(clientPlayer,player);

            //clientPlayer.sendObject(new GetToSendInfo(info.getKeepPlayer(),EntityHandler.playerClientMap.get(clientPlayer)));
        }
    }

}