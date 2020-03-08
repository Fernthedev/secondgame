package com.github.fernthedev.game.deprecated.server;

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
        }
    }

}
