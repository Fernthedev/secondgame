package io.github.fernthedev.secondgame.main.netty.client

import com.github.fernthedev.lightchat.client.api.IPacketHandler
import com.github.fernthedev.lightchat.client.event.ServerConnectFinishEvent
import com.github.fernthedev.lightchat.client.event.ServerDisconnectEvent
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler
import com.github.fernthedev.lightchat.core.api.event.api.Listener
import com.github.fernthedev.lightchat.core.packets.Packet
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.packets.LevelUp
import com.github.fernthedev.packets.object_updates.SendObjectsList
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.packets.player_updates.ClientWorldUpdatePacket
import com.github.fernthedev.universal.entity.EntityPlayer
import com.github.fernthedev.universal.entity.NewGsonGameObject
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu
import java.util.*

class ClientPacketHandler : IPacketHandler, Listener {
    override fun handlePacket(p: Packet, packetId: Int) {
        when (p) {
            is SetCoin -> {
                Game.loggerImpl.info("Coin one up")
                Game.mainPlayer?.coin = (p.coins)
            }

            is SendObjectsList -> {
                val gameObjects = p.objectList
                val universalPlayer = p.mainPlayer.toGameObject() as EntityPlayer
                Game.loggerImpl.info("Updated world")

                gameObjects.forEach { (uuid: UUID, newGsonGameObject: NewGsonGameObject?) ->
                    if (newGsonGameObject == null) {
                        Game.staticEntityRegistry.removeEntityObject(uuid)
                        return@forEach
                    }

                    try {
                        val `object` = newGsonGameObject.toGameObject()

                        Game.staticEntityRegistry.addEntityObject(`object`)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                }

                Game.mainPlayer = universalPlayer

                Game.client!!.sendObject(
                    ClientWorldUpdatePacket(
                        NewGsonGameObject(Game.mainPlayer!!),
                        Game.staticEntityRegistry.objectsAndHashCode
                    )
                )
            }

            is GameOverPacket -> {
                Game.client?.disconnect()
                Game.mainPlayer?.health = 100
                Game.screen = EndScreen()
            }

            is LevelUp -> {
                Game.hud.level = p.level
            }
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