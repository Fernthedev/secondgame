package com.github.fernthedev.game.deprecated.server.netty;


import com.github.fernthedev.game.deprecated.server.*;
import com.github.fernthedev.game.server.game_handler.ServerGameHandler;
import com.github.fernthedev.packets.player_updates.SetCurrentPlayer;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.Velocity;
import com.github.fernthedev.universal.entity.EntityPlayer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class ProcessingHandler extends ChannelInboundHandlerAdapter {


    private final Server server;

    public ProcessingHandler(Server server) {this.server = server;}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Packet requestData = (Packet) msg;

        EventListener eventListener = new EventListener(Server.socketList.get(ctx.channel()));
        eventListener.received(requestData);


        ctx.fireChannelRead(msg);

    }

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

            EntityPlayer universalPlayer = new EntityPlayer(Server.WIDTH / 2 - 32, Server.HEIGHT / 2 - 32, EntityID.Player,new Velocity(0),new Velocity(0), GameObject.entities);
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



            ServerGameHandler.getEntityHandler().addPlayerEntityObject(clientPlayer, universalPlayer);

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
        Map<Channel, ClientPlayer> sockets = new HashMap<>(Server.socketList);
        ClientPlayer clientPlayer = sockets.get(ctx.channel());

        if(clientPlayer != null) {
            clientPlayer.close();
            ServerGameHandler.getEntityHandler().removePlayerEntityObject(clientPlayer.getPlayerObject());
        }
        ctx.close();

        Server.socketList.remove(ctx.channel());
        Server.channelServerHashMap.remove(ctx.channel());
    }

}
