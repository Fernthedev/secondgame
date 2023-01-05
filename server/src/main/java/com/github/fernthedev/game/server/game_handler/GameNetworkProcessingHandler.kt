package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.game.server.ClientGameData
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.lightchat.core.ColorCode
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler
import com.github.fernthedev.lightchat.core.api.event.api.Listener
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.lightchat.server.event.PlayerDisconnectEvent
import com.github.fernthedev.lightchat.server.event.PlayerJoinEvent
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.EntityPlayer
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInboundHandlerAdapter
import java.util.*

@ChannelHandler.Sharable
class GameNetworkProcessingHandler(
    val server: GameServer
) : ChannelInboundHandlerAdapter(), Listener {
    @EventHandler
    fun playerJoin(e: PlayerJoinEvent) {
        server.server.logger
            .info(ColorCode.YELLOW.toString() + "handling player " + e.getJoinPlayer().getName() + " giving data")
        val clientConnection: ClientConnection = e.joinPlayer
        val universalPlayer = EntityPlayer(
            Location(
            UniversalHandler.WIDTH.toFloat() / 2f - 32f,
            (UniversalHandler.HEIGHT.toFloat() / 2f - 32f),
            ),
            clientConnection.name
        )
        server.serverGameHandler.entityHandler.addClientData(
            clientConnection, ClientGameData(clientConnection, universalPlayer.uniqueId, universalPlayer), Objects.hash(
                Any()
            )
        )

        server.serverGameHandler.entityHandler.finishEntityUpdate()
    }

    @EventHandler
    fun onLeave(e: PlayerDisconnectEvent) {
        server.serverGameHandler.entityHandler.removeClientData(e.disconnectedPlayer)
        StaticHandler.getCore().logger.debug("Removed player {}", e.disconnectedPlayer.name)
    }
}