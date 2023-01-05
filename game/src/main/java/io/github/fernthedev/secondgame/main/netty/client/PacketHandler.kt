package io.github.fernthedev.secondgame.main.netty.client

import com.github.fernthedev.CommonUtil
import com.github.fernthedev.lightchat.client.api.IPacketHandler
import com.github.fernthedev.lightchat.client.event.ServerConnectFinishEvent
import com.github.fernthedev.lightchat.client.event.ServerDisconnectEvent
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler
import com.github.fernthedev.lightchat.core.api.event.api.Listener
import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.packets.LevelUp
import com.github.fernthedev.packets.object_updates.AddObjectPacket
import com.github.fernthedev.packets.object_updates.RemoveObjectPacket
import com.github.fernthedev.packets.object_updates.SendObjectsList
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.packets.player_updates.SendToClientPlayerInfoPacket
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket
import com.github.fernthedev.universal.entity.NewGsonGameObject
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

class PacketHandler : IPacketHandler, Listener {
    override fun handlePacket(p: Packet, packetId: Int) {
        when (p) {
            is SendToClientPlayerInfoPacket -> {
                val universalPlayer = p.playerObject
                Game.mainPlayer = universalPlayer
                Game.staticEntityRegistry.addEntityObject(Game.mainPlayer!!)
                Game.mainPlayer!!.health = universalPlayer.health
                Game.client?.sendObject(
                    SendToServerPlayerInfoPacket(
                        Game.mainPlayer!!,
                        Game.staticEntityRegistry.objectsAndHashCode
                    )
                )
            }

            is SetCoin -> {
                Game.loggerImpl.info("Coin one up")
                Game.mainPlayer?.coin = (p.coins)
            }

            is SendObjectsList -> {
                val list = p
                StaticHandler.getCore().logger.debug(
                    "Updating object list {}\n",
                    list.objectList.values.map { it?.clazz }.toList()
                )
                val gameObjects = list.objectList
                val universalPlayer = list.mainPlayer
                val doUpdate = AtomicBoolean(true)
                gameObjects.forEach { (uuid: UUID?, newGsonGameObject: NewGsonGameObject?) ->
                    try {
                        if (newGsonGameObject == null) {
                            Game.staticEntityRegistry.removeEntityObject(uuid)
                        } else {
                            val `object` = newGsonGameObject.toGameObject()
                            if (`object` == null) Game.staticEntityRegistry.removeEntityObject(uuid) else {
                                var updateEntity = true
                                val isPlayer =
                                    Game.mainPlayer != null && `object`.uniqueId === Game.mainPlayer?.uniqueId
                                if (// If the packet received is 3 packets old
                                    isPlayer && Game.client!!.getPacketId(p.javaClass)
                                        .left - 3 > packetId && System.currentTimeMillis() - Game.client!!.getPacketId(p.javaClass)
                                        .right > 900
                                ) updateEntity = false
                                if (isPlayer && (abs(universalPlayer.location.x - Game.mainPlayer!!.location.x) > CommonUtil.PLAYER_COORD_DIF ||
                                            abs(universalPlayer.location.y - Game.mainPlayer!!.location.y) > CommonUtil.PLAYER_COORD_DIF)
                                ) updateEntity = false
                                if (updateEntity) {
                                    Game.staticEntityRegistry.addEntityObject(`object`)
                                }
                                if (isPlayer) doUpdate.set(updateEntity)
                            }
                        }
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }
                if (gameObjects.containsKey(universalPlayer.uniqueId)) {
                    if (Game.mainPlayer == null || Game.mainPlayer.hashCode() != universalPlayer.hashCode()) {
                        Game.staticEntityRegistry.addEntityObject(
                            universalPlayer
                        )
                    }
                }
                if (doUpdate.get()) Game.mainPlayer = universalPlayer

                StaticHandler.getCore().logger.debug("Updating player because server asked us to $universalPlayer")
            }

            is AddObjectPacket -> {
                val (newGsonGameObject) = p
                try {
                    val `object` = newGsonGameObject.toGameObject()!!
                    Game.staticEntityRegistry.addEntityObject(`object`)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            is RemoveObjectPacket -> {
                val (uuid) = p
                Game.staticEntityRegistry.removeEntityObject(uuid)
            }
        }
        if (p is GameOverPacket) {
            Objects.requireNonNull(Game.client!!).disconnect()
            Game.mainPlayer!!.health = (100)
            Game.screen = EndScreen()
        } else if (p is LevelUp) {
            Game.hud.level = p.level
        }
    }

    @EventHandler
    fun onConnect(e: ServerConnectFinishEvent?) {
    }

    @EventHandler
    fun onDisconnect(e: ServerDisconnectEvent?) {
        resetGame()
        Game.client = null
    }

    private fun resetGame() {
        Game.screen = MainMenu()
        if (Game.mainPlayer != null) Game.mainPlayer!!.health = 100
    }
}