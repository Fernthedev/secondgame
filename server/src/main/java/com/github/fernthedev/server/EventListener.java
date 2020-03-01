package com.github.fernthedev.server;

import com.github.fernthedev.packets.PlayerUpdates.SendPlayerInfoPacket;
import com.github.fernthedev.packets.PlayerUpdates.SendToGetInfo;
import com.github.fernthedev.packets.PongPacket;
import com.github.fernthedev.server.gameHandler.ServerGameHandler;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.Velocity;
import com.github.fernthedev.universal.entity.EntityPlayer;

import java.util.concurrent.TimeUnit;
public class EventListener {

    private ClientPlayer clientPlayer;

    public EventListener(ClientPlayer clientPlayer) {
     this.clientPlayer = clientPlayer;
    }

    public void received(Object p) {

        //System.out.println(p + " is the packet");

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

            EntityPlayer oldPlayer = clientPlayer.getPlayerObject();

            if(oldPlayer == null) oldPlayer = new EntityPlayer(Server.WIDTH / 2 - 32, Server.HEIGHT / 2 - 32, ID.Player,new Velocity(0),new Velocity(0), GameObject.entities);

            EntityPlayer player = new EntityPlayer(info.getKeepPlayer(), oldPlayer);

            UniversalHandler.getThingHandler().updatePlayerObject(clientPlayer,player);

            //clientPlayer.sendObject(new GetToSendInfo(info.getKeepPlayer(),EntityHandler.playerClientMap.get(clientPlayer)));
        }
    }

}
