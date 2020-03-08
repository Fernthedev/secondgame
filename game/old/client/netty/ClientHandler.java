package io.github.fernthedev.secondgame.main.deprecated.netty.client.netty;

import com.github.fernthedev.core.packets.Packet;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.MenuParticle;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.deprecated.netty.client.Client;
import io.github.fernthedev.secondgame.main.deprecated.netty.client.EventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

import static io.github.fernthedev.secondgame.main.Game.HEIGHT;
import static io.github.fernthedev.secondgame.main.Game.WIDTH;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final EventListener listener;

    public ClientHandler(Client client, EventListener listener) {
        this.listener = listener;
        this.client = client;
    }

    private final Client client;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        client.registered = true;

        //ChannelFuture future = ctx.writeAndFlush(new ConnectedPacket(client.name));
        System.out.println("Sent connect packet for request");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof Packet) {
            Packet packet = (Packet) msg;
            listener.recieved(packet);

           // if (!(msg instanceof PingPacket))
                //System.out.println("Packet received which is " + msg);
        }

        //ctx.close();
    }

    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("Unregistered");
        resetGame();
    }

    private void resetGame() {
        Game.getHandler().clearObjects();
        Game.gameState = Game.STATE.MENU;
        UniversalHandler.mainPlayer.setHealth(100);
        Random r = new Random();
        int amount = r.nextInt(15);
        if (amount < 10) amount = 10;
        for (int i = 0; i < amount; i++) {

            Game.getHandler().addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), EntityID.MenuParticle, GameObject.entities));
        }

        Client.getClientThread().disconnect();
    }
}
