package com.github.fernthedev.server;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.server.gameHandler.EntityHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.netty.channel.Channel;

import static com.github.fernthedev.server.Server.socketList;

public class ClientPlayer {


    //private ObjectOutputStream out;
    //private ObjectInputStream in;

    private FernThread thread;

    private boolean connected;

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    private UniversalPlayer playerObject;

    public Channel channel = null;

    public void setThread(FernThread thread) {
        this.thread = thread;
    }

    public boolean isConnected() {
        return connected;
    }

    public ClientPlayer(Channel channel,UniversalPlayer universalPlayer) {
        this.channel = channel;
        playerObject = universalPlayer;
        connected = true;
    }

    protected ClientPlayer() {}

        void setLastPacket(Object packet) {

        }

    public synchronized void sendObject(Object packet) {
        if (packet instanceof Packet && channel != null) {
            //System.out.println("Sending packet");
            channel.writeAndFlush(packet);
            // out.flush();
           /* if(!(packet instanceof PingPacket)) {
                System.out.println("Sent " + packet);
            }*/

        }else {
            System.out.println("not packet");
        }
    }

    public synchronized void close() {
        try {
            thread.running = false;
            System.out.println("Closing player " + this.toString());
            //DISCONNECT FROM SERVER
            //RemovePlayerPacket packet = new RemovePlayerPacket();
            if(channel != null) {
                if (channel.isOpen()) {
                    channel.closeFuture();

                    connected = false;

                    socketList.remove(channel);


                }
            }
            //if(!scanner.nextLine().equals(""))



            connected = false;
            thread.join();

            //serverSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public UniversalPlayer getPlayerObject() {
        return playerObject;
    }



    public synchronized void setPlayerObject(UniversalPlayer playerObject) {
        this.playerObject = playerObject;
    }

    @Override
    public String toString() {

        return "[ClientPlayer] IP: " + getAdress() + " name " + playerObject;
    }



        String getAdress() {
        if(channel == null || channel.remoteAddress() == null) {
            return "unknown";
        }

            return channel.remoteAddress().toString();
        }

        public static ClientPlayer getPlayerFromObject(int id) {
        ClientPlayer clientPlayerReturn = null;


            for(ClientPlayer clientPlayer : EntityHandler.playerClientMap.keySet()) {
                if(clientPlayer.getPlayerObject().getObjectID() == id) return clientPlayer;
            }

            return null;
        }

        public static ClientPlayer getPlayerFromObject(UniversalPlayer universalPlayer) {
            for (ClientPlayer clientPlayer : EntityHandler.playerClientMap.keySet()) {
                if (clientPlayer.getPlayerObject() == universalPlayer) return clientPlayer;
            }

            return null;
        }


}
