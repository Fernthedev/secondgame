package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.game.server.ClientGameData
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.lightchat.core.ColorCode
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.lightchat.server.event.PlayerDisconnectEvent
import com.github.fernthedev.lightchat.server.event.PlayerJoinEvent
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.EntityPlayer
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelInboundHandlerAdapter

@ChannelHandler.Sharable
class GameNetworkProcessingHandler(
    val server: GameServer
) : ChannelInboundHandlerAdapter() {

    fun playerJoin(e: PlayerJoinEvent) {
        server.server.logger
            .info(ColorCode.YELLOW.toString() + "handling player " + e.joinPlayer.name + " giving data")
        val clientConnection: ClientConnection = e.joinPlayer
        val universalPlayer = EntityPlayer(
            Location(
                UniversalHandler.WIDTH.toFloat() / 2f - 32f,
                (UniversalHandler.HEIGHT.toFloat() / 2f - 32f),
            ),
            clientConnection.name,
        )
        server.serverGameHandler.entityHandler.addClientData(
            clientConnection, ClientGameData(clientConnection, universalPlayer.uniqueId, universalPlayer)
        )
    }


    fun onLeave(e: PlayerDisconnectEvent) {
        server.serverGameHandler.entityHandler.removeClientData(e.disconnectedPlayer)
        StaticHandler.core.logger.debug("Removed player {}", e.disconnectedPlayer.name)
    }
}