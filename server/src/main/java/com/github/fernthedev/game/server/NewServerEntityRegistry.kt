package com.github.fernthedev.game.server

import com.github.fernthedev.GameMathUtil
import com.github.fernthedev.INewEntityRegistry
import com.github.fernthedev.lightchat.core.ColorCode
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.core.encryption.transport
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.packets.player_updates.ClientWorldUpdatePacket
import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.entity.EntityPlayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap


class NewServerEntityRegistry(
    val server: GameServer
) : INewEntityRegistry() {

    val clientGameDataMap: MutableMap<ClientConnection, ClientGameData> = ConcurrentHashMap()

    val isClientDataEmpty: Boolean
        get() = clientGameDataMap.isEmpty()

    fun addClientData(connection: ClientConnection, clientGameData: ClientGameData) {
        clientGameData.clientSidePlayerHashCode = -1

        clientGameDataMap[connection] = clientGameData
        addEntityObject(clientGameData.entityPlayer)

        clientGameData.forcedUpdate = true
    }

    fun removeClientData(connection: ClientConnection) {
        clientGameDataMap.remove(connection)
        removeEntityObject(connection.uuid)
    }

    private fun updatePlayerObject(clientPlayerE: ClientConnection, universalPlayer: EntityPlayer) {
        if (!gameObjects.containsKey(universalPlayer.uniqueId)) {
            StaticHandler.core.logger
                .debug(ColorCode.RED.toString() + "Player updating but is removed from game")
            return
        }


        clientGameDataMap[clientPlayerE]!!.entityPlayer = universalPlayer
        addEntityObject(universalPlayer)
        StaticHandler.core.logger.debug("Attempting to update info {}", universalPlayer)
    }

    override suspend fun tick() = coroutineScope {
        super.tick()
    }

    override fun collisionCheck(universalPlayer: EntityPlayer) = runBlocking {
        clientGameDataMap.keys
            .filter { connection: ClientConnection -> clientGameDataMap[connection]!!.entityPlayer.uniqueId === universalPlayer.uniqueId }
            .forEach { connection: ClientConnection ->
                val clientGameData = clientGameDataMap[connection]
                var coinIncrease = 0

                gameObjects.values
                    .filter { gameObject: GameObject ->
                        gameObject.bounds.intersects(
                            universalPlayer.bounds
                        )
                    }
                    .forEach { tempObject: GameObject ->
                        if (tempObject.entityId == EntityID.ENEMY) {
                            //COLLISION CODE
                            universalPlayer.health = universalPlayer.health - 2
                        }
                        if (tempObject.entityId == EntityID.COIN) {
                            coinIncrease++
                            removeEntityObject(tempObject)
                        }
                    }

                if (coinIncrease > 0) {
                    universalPlayer.coin = universalPlayer.coin + coinIncrease

                    connection.sendObjectIO(SetCoin(universalPlayer.coin).transport())
                }

                if (universalPlayer.health <= 0) {
                    removeEntityObject(clientGameData!!.entityPlayer)
                }
            }
    }


    fun removeRespawnAllPlayers() {
        clearEntities()
        clientGameDataMap.forEach { (_: ClientConnection?, clientGameData: ClientGameData) ->
            clientGameData.entityPlayer.health = 100
            addEntityObject(clientGameData.entityPlayer)
        }
    }

    fun handleClientRespond(clientPlayer: ClientConnection, infoPacket: ClientWorldUpdatePacket) {
        val clientData: ClientGameData = server.serverGameHandler.entityHandler.clientGameDataMap[clientPlayer]
            ?: return
        val oldPlayer: EntityPlayer = clientData.entityPlayer
        val packetPlayer: EntityPlayer = infoPacket.playerObject.toGameObject() as EntityPlayer
        val velXClamp: Float = GameMathUtil.clamp(
            packetPlayer.velX,
            -EntityPlayer.MAX_VELOCITY.toFloat(),
            EntityPlayer.MAX_VELOCITY.toFloat()
        )
        val velYClamp: Float = GameMathUtil.clamp(
            packetPlayer.velY,
            -EntityPlayer.MAX_VELOCITY.toFloat(),
            EntityPlayer.MAX_VELOCITY.toFloat()
        )

        val copyNewPlayer = EntityPlayer(
            Location(
                oldPlayer.location.x,
                oldPlayer.location.y
            ),
            uniqueId = oldPlayer.uniqueId,
            velX = velXClamp,
            velY =
            velYClamp,
            color =
            oldPlayer.color,
            health =
            oldPlayer.health,
            coin =
            oldPlayer.coin,
            name =
            oldPlayer.name
        )
        val clientGameData = clientGameDataMap[clientPlayer]!!
        clientGameData.clientSidePlayerHashCode = packetPlayer.hashCode()
        clientGameData.clientSideLocation = packetPlayer.location
        clientGameData.objectCacheList.clear()
        clientGameData.objectCacheList.putAll(infoPacket.entitiesHashCodeMap!!)

        if (EntityPlayer.isPlayerDifferent(oldPlayer, copyNewPlayer, velXClamp, velYClamp)) {
            StaticHandler.core.logger.debug("Client player is changed")
        }

        updatePlayerObject(clientPlayer, copyNewPlayer)
    }
}