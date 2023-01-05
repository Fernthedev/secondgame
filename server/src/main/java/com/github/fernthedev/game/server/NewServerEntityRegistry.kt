package com.github.fernthedev.game.server

import com.github.fernthedev.GameMathUtil
import com.github.fernthedev.INewEntityRegistry
import com.github.fernthedev.lightchat.core.ColorCode
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.packets.object_updates.SetCoin
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket
import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.entity.EntityPlayer
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer


class NewServerEntityRegistry(
    val server: GameServer
) : INewEntityRegistry() {

    val clientGameDataMap: MutableMap<ClientConnection, ClientGameData> = ConcurrentHashMap()

    val isClientDataEmpty: Boolean
        get() = clientGameDataMap.isEmpty()

    private fun getClientData(connection: ClientConnection): ClientGameData? {
        return clientGameDataMap.getOrDefault(connection, null)
    }

    fun addClientData(connection: ClientConnection, clientGameData: ClientGameData, entityHashCode: Int) {
        clientGameData.clientSidePlayerHashCode = entityHashCode

        clientGameDataMap[connection] = clientGameData
        addEntityObject(clientGameData.entityPlayer)
    }

    fun removeClientData(connection: ClientConnection) {
        clientGameDataMap.remove(connection)
        removeEntityObject(connection.uuid)
    }

    fun updatePlayerObject(clientPlayerE: ClientConnection, universalPlayer: EntityPlayer) {
        if (!gameObjects.containsKey(universalPlayer.uniqueId)) {
            StaticHandler.getCore().logger
                .debug(ColorCode.RED.toString() + "Player updating but is removed from game")
            return
        }
        getClientData(clientPlayerE)!!.entityPlayer = universalPlayer
        addEntityObject(universalPlayer)
        StaticHandler.getCore().logger.debug("Attempting to update info {}", universalPlayer)
    }

    override suspend fun tick() = coroutineScope {
        super.tick()
        if (clientGameDataMap.isNotEmpty()) finishEntityUpdate()
    }

    override fun collisionCheck(universalPlayer: EntityPlayer) {
        clientGameDataMap.keys
            .filter { connection: ClientConnection -> clientGameDataMap[connection]!!.entityPlayer.uniqueId === universalPlayer.uniqueId }
            .forEach { connection: ClientConnection ->
                val clientGameData = getClientData(connection)
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
                            connection.sendObject(SendToServerPlayerInfoPacket(universalPlayer, null))
                        }
                        if (tempObject.entityId == EntityID.COIN) {
                            universalPlayer.coin = universalPlayer.coin + 1
                            connection.sendObject(SetCoin(universalPlayer.coin))
                            removeEntityObject(tempObject)
                            StaticHandler.getCore().logger.debug("Collision checking! COIN")
                            // this.handler.removeObject(tempObject);
                        }
                    }
                if (universalPlayer.health <= 0) {
                    removeEntityObject(clientGameData!!.entityPlayer)
                }
            }
    }

    fun finishEntityUpdate() {
        server.playerPollUpdateThread.sendUpdate.set(true)
    }

    fun removeRespawnAllPlayers() {
        gameObjects.clear()
        clientGameDataMap.forEach(BiConsumer<ClientConnection, ClientGameData> { connection: ClientConnection?, clientGameData: ClientGameData ->
            clientGameData.entityPlayer.health = 100
            addEntityObject(clientGameData.entityPlayer)
        })
        server.serverGameHandler.entityHandler.finishEntityUpdate()
    }

    fun handleClientRespond(clientPlayer: ClientConnection, infoPacket: SendToServerPlayerInfoPacket) {
        val clientData: ClientGameData = server.serverGameHandler.entityHandler.getClientData(clientPlayer)
            ?: return
        val oldPlayer: EntityPlayer = clientData.entityPlayer
        val packetPlayer: EntityPlayer = infoPacket.playerObject
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
                oldPlayer.location.x + velXClamp,
                oldPlayer.location.y + velYClamp
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
        clientGameData.clientSidePlayerHashCode = (infoPacket.playerObject.hashCode())
        clientGameData.objectCacheList.clear()
        clientGameData.objectCacheList.putAll(infoPacket.entitiesHashCodeMap!!)

        if (EntityPlayer.isPlayerDifferent(oldPlayer, copyNewPlayer, velXClamp, velYClamp)) {
            StaticHandler.getCore().logger.debug("Client player is changed")
        }

        updatePlayerObject(clientPlayer, copyNewPlayer)
    }
}