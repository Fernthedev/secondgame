package com.github.fernthedev.server.netty;


import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.packets.PlayerUpdates.SetCurrentPlayer;
import com.github.fernthedev.server.*;
import com.github.fernthedev.server.gameHandler.EntityHandler;
import com.github.fernthedev.server.gameHandler.ServerGameHandler;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.Velocity;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ProcessingHandler extends ChannelInboundHandlerAdapter {


    private final Server server;

    public ProcessingHandler(Server server) {this.server = server;}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        System.out.println(msg);
        Packet requestData = (Packet) msg;

        /*if(eventListener == null) {
            packetsLost.add(msg);
        }*/
        /*boolean found = false;
        for(Channel channel : EntityHandler.playerMap.keySet()) {
            if(channel == ctx.channel()) {
              //  System.out.println("Found the current channel");
                found = true;
            }
        }

        if(!found) {
            System.out.println("No channel associated with me?");
        }*/



        EventListener eventListener = new EventListener(Server.socketList.get(ctx.channel()));
        eventListener.recieved(requestData);

       // System.out.println(ctx.channel());

        //eventListener.recieved(requestData);

        //ChannelFuture future = ctx.writeAndFlush(responseData);
        //future.addListener(ChannelFutureListener.CLOSE);
        /*if(!(requestData instanceof PongPacket))
        System.out.println("Received this packet " + msg);*/
        ctx.fireChannelRead(msg);
        //ctx.flush();
    }
    /*
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        ctx.fireChannelReadComplete();
    }*/

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        if(!ctx.channel().isActive()) {
            try {
                ctx.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Channel Registering");
        Channel channel = ctx.channel();

        if (channel != null) {
            Server server = Server.channelServerHashMap.get(ctx.channel());

            UniversalPlayer universalPlayer = new UniversalPlayer(Server.WIDTH / 2 - 32, Server.HEIGHT / 2 - 32, ID.Player,new Velocity(0),new Velocity(0), GameObject.entities);
            ClientPlayer clientPlayer = new ClientPlayer(channel,universalPlayer);
            clientPlayer.setConnected(true);

            System.out.println("Created clientPlayer variable " + clientPlayer + " channel " + channel);

            Server.socketList.put(channel,clientPlayer);

            System.out.println(Server.socketList);

            //EventListener listener = new EventListener(server, clientPlayer);
            //processingHandler.setListener(listener);
            System.out.println("Events registered");

// And From your main() method or any other method
            FernThread runningFernThread;


            ServerThread serverThread = new ServerThread(server, channel, clientPlayer);

            runningFernThread = new FernThread(serverThread);
            clientPlayer.setThread(runningFernThread);

            runningFernThread.startThread();
            System.out.println(runningFernThread + " is from " + serverThread);
            UniversalHandler.threads.add(runningFernThread);

            //serverThread.startListener();

            System.out.println("Thread started for player " + clientPlayer);



            ServerGameHandler.getEntityHandler().addPlayerEntityObject(clientPlayer,universalPlayer);
            EntityHandler.playerMap.put(channel,universalPlayer);

                clientPlayer.sendObject(new SetCurrentPlayer(universalPlayer));



            //clientPlayer.sendObject(new RequestNamePacket());
/*
            Runtime.getRuntime().addShutdownHook(new FernThread() {
                @Override
                public void run() {
                    for (ServerThread serverThread : Server.serverThreads) {
                        if (serverThread.clientPlayer.channel.isOpen()) {
                            System.out.println("Gracefully shutting down/");
                            serverThread.clientPlayer.close();
                        }
                    }
                }
            });*/
        }else{
            System.out.println("Channel is null");
            throw new NullPointerException();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {


        if(!cause.getMessage().contains("An existing connection was forcibly closed by the remote host"))
        ctx.fireExceptionCaught(cause);

        close(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        close(ctx);
        System.out.println("Inactive");
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
            close(ctx);
            System.out.println("Unregistered");
    }

    private void close(ChannelHandlerContext ctx) {
        ClientPlayer clientPlayer = Server.socketList.get(ctx.channel());

        if(clientPlayer != null)
        clientPlayer.close();

        ctx.close();

        Server.socketList.remove(ctx.channel());
        Server.channelServerHashMap.remove(ctx.channel());
        EntityHandler.playerMap.remove(ctx.channel());
        EntityHandler.playerClientMap.remove(clientPlayer);
        ServerGameHandler.getEntityHandler().removePlayerEntityObject(clientPlayer);
    }

}
