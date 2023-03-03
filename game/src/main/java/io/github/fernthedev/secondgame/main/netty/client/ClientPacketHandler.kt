package io.github.fernthedev.secondgame.main.netty.client

import com.github.fernthedev.lightchat.client.api.IPacketHandler
import com.github.fernthedev.lightchat.core.codecs.AcceptablePacketTypes
import com.github.fernthedev.lightchat.core.packets.PacketProto
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.packets.LevelUp
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.packets.proto.Packets
import com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket
import com.github.fernthedev.toGameObject
import com.github.fernthedev.universal.entity.EntityPlayer
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu
import java.util.*

class ClientPacketHandler : IPacketHandler {
    override fun handlePacket(acceptablePacketTypes: AcceptablePacketTypes, packetId: Int) {

        val protoWrapper = acceptablePacketTypes as? PacketProto

        val protoPacket = protoWrapper?.message
        if (protoPacket is SendObjectsListPacket) {
            val gameObjects = protoPacket.objectListMap
            val universalPlayer = protoPacket.mainPlayer.toGameObject() as EntityPlayer

            gameObjects.forEach { (uuid: String, newGsonGameObject: Packets.GameObjectNullable) ->
                val go = newGsonGameObject.toGameObject()


                if (go == null) {
                    Game.staticEntityRegistry.removeEntityObject(UUID.fromString(uuid))
                    return@forEach
                }

                try {

                    Game.staticEntityRegistry.addEntityObject(go)
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }

            val loc = Game.mainPlayer?.location

            // Don't teleport unnecessarily
            if (!protoPacket.teleport && loc != null) {
                universalPlayer.location.copyFrom(loc)
            }

            Game.mainPlayer = universalPlayer
        }

        when (acceptablePacketTypes) {
            is SetCoin -> {
                Game.loggerImpl.info("Coin one up")
                Game.mainPlayer?.coin = (acceptablePacketTypes.coins)
            }


            is GameOverPacket -> {
                Game.client?.disconnect()
                Game.mainPlayer?.health = 100
                Game.screen = EndScreen()
            }

            is LevelUp -> {
                Game.hud.level = acceptablePacketTypes.level
            }
        }
    }


    fun onDisconnect() {
        resetGame()
        Game.client = null
    }

    private fun resetGame() {
        Game.screen = MainMenu()
        if (Game.mainPlayer != null) Game.mainPlayer!!.health = 100
    }
}