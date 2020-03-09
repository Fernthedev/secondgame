package io.github.fernthedev.secondgame.main.deprecated.netty.client;


import java.util.concurrent.TimeUnit;

@Deprecated
public class EventListener {

    EventListener(Client client) {
    }

    public void recieved(Object p) {
        if(p instanceof PingPacket) {
            //System.out.println("Ponged!");
            PingPacket packet = (PingPacket) p;

            long time = (System.nanoTime() - packet.getTime() ) / 1000000;

            System.out.println("Ping: " + TimeUnit.MILLISECONDS.convert(time,TimeUnit.NANOSECONDS) + " ms");
            Client.getClientThread().sendObject(new PongPacket());
        }
    }

}
