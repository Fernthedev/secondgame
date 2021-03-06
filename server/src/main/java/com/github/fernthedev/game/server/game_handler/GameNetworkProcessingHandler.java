package com.github.fernthedev.game.server.game_handler;

import com.github.fernthedev.game.server.ClientGameData;
import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.lightchat.core.ColorCode;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler;
import com.github.fernthedev.lightchat.core.api.event.api.Listener;
import com.github.fernthedev.lightchat.server.ClientConnection;
import com.github.fernthedev.lightchat.server.event.PlayerDisconnectEvent;
import com.github.fernthedev.lightchat.server.event.PlayerJoinEvent;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@ChannelHandler.Sharable
@RequiredArgsConstructor
public class GameNetworkProcessingHandler extends ChannelInboundHandlerAdapter implements Listener {

    private final GameServer server;

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {

        server.getServer().getLogger().info(ColorCode.YELLOW + "handling player " + e.getJoinPlayer().getName() + " giving data");
        ClientConnection clientConnection = e.getJoinPlayer();

        EntityPlayer universalPlayer = new EntityPlayer((float) UniversalHandler.WIDTH / 2 - 32, (float) UniversalHandler.HEIGHT / 2 - 32, clientConnection.getName());

        server.getServerGameHandler().getEntityHandler().addClientData(clientConnection, new ClientGameData(clientConnection, universalPlayer.getUniqueId(), universalPlayer), Objects.hash(new Object()));

//        server.getServerGameHandler().getEntityHandler().updatePlayerObject(clientConnection, universalPlayer);


//        clientConnection.sendObject(new SendToServerPlayerInfoPacket(universalPlayer));
        server.getServerGameHandler().getEntityHandler().finishEntityUpdate();
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        server.getServerGameHandler().getEntityHandler().removeClientData(e.getDisconnectedPlayer());

        StaticHandler.getCore().getLogger().debug("Removed player {}", e.getDisconnectedPlayer().getName());
    }

}
