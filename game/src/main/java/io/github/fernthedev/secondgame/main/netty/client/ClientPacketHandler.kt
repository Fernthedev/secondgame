package io.github.fernthedev.secondgame.main.netty.client

import com.github.fernthedev.lightchat.client.api.IPacketHandler
import com.github.fernthedev.lightchat.client.event.ServerDisconnectEvent
import com.github.fernthedev.lightchat.core.packets.PacketJSON
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.packets.LevelUp
import com.github.fernthedev.packets.object_updates.SendObjectsList
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.universal.entity.EntityPlayer
import com.github.fernthedev.universal.entity.NewGsonGameObject
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu
import java.util.*

class ClientPacketHandler : IPacketHandler {
    override fun handlePacket(packet: PacketJSON, packetId: Int) {
        when (packet) {
            is SetCoin -> {
                Game.loggerImpl.info("Coin one up")
                Game.mainPlayer?.coin = (packet.coins)
            }

            is SendObjectsList -> {
                val gameObjects = packet.objectList
                val universalPlayer = packet.mainPlayer.toGameObject() as EntityPlayer

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

                val loc = Game.mainPlayer?.location

                // Don't teleport unnecessarily
                if (!packet.teleport && loc != null) {
                    universalPlayer.location.copyFrom(loc)
                }

                Game.mainPlayer = universalPlayer
            }

            is GameOverPacket -> {
                Game.client?.disconnect()
                Game.mainPlayer?.health = 100
                Game.screen = EndScreen()
            }

            is LevelUp -> {
                Game.hud.level = packet.level
            }
        }
    }




    fun onDisconnect(e: ServerDisconnectEvent) {
        resetGame()
        Game.client = null
    }

    private fun resetGame() {
        Game.screen = MainMenu()
        if (Game.mainPlayer != null) Game.mainPlayer!!.health = 100
    }
}