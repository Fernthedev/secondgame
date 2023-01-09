package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.game.server.ClientGameData
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.game.server.NewServerEntityRegistry
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.core.encryption.transport
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.packets.object_updates.SendObjectsList
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.approx
import com.github.fernthedev.universal.entity.EntityPlayer
import com.github.fernthedev.universal.entity.NewGsonGameObject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.system.measureTimeMillis

/**
 * This thread watches for updates
 * on the server and sends player updates
 *
 */
class PlayerUpdateHandler(private val gameServer: GameServer) {

    suspend fun update(): Unit = coroutineScope {
        val entityHandler: NewServerEntityRegistry = gameServer.serverGameHandler.entityHandler
        val clientConnections = entityHandler.clientGameDataMap.keys
        val cachedObjectMap: Map<UUID, GameObject> = gameServer.entityRegistry.gameObjects

        val time = measureTimeMillis {

            val jsonGameObjects =
                cachedObjectMap.map { (uuid: UUID, gameObject: GameObject) ->
                    return@map uuid to lazy { NewGsonGameObject(gameObject) to gameObject.hashCode() }
                }.toMap()


            val jobs = clientConnections.map { clientConnection ->
                launch {
                    updatePlayerInfo(clientConnection, jsonGameObjects)
                }
            }

            jobs.forEach {
                it.join()
            }
        }


        if (time > 10) {
            StaticHandler.core.logger.debug(
                "Updating players took {}", time
            )
        }


    }

    /**
     *
     * @param clientPlayer
     * @return true if sent an update
     */
    private fun updatePlayerInfo(
        clientPlayer: ClientConnection,
        jsonGameObjects: Map<UUID, Lazy<Pair<NewGsonGameObject, Int>>>
    ) {
        val clientGameData: ClientGameData = gameServer.entityRegistry.clientGameDataMap[clientPlayer]!!
        val serverObjects: Map<UUID, GameObject> = gameServer.entityRegistry.gameObjects


        val clientChangedObjects: Map<UUID, NewGsonGameObject?> = if (clientGameData.forcedUpdate) {
            jsonGameObjects.map { it.key to it.value.value.first }.toMap()
        } else {
            serverObjects
                // new or updated objects
                .map { (uuid: UUID, gameObject: GameObject) ->
                    val serverObject = jsonGameObjects[uuid]!!
                    // added objects
                    val clientObject =
                        clientGameData.objectCacheList[uuid] ?: return@map uuid to serverObject.value.first

                    // updated objects
                    var changed = clientObject != serverObject.value.second
                    if (uuid == clientGameData.entityPlayer.uniqueId) {
                        changed =
                            changed || clientGameData.clientSidePlayerHashCode != clientGameData.entityPlayer.hashCode() ||
                                    EntityPlayer.isPlayerDifferent(
                                        clientGameData.entityPlayer,
                                        gameObject as EntityPlayer,
                                        0.0F,
                                        0.0F
                                    )
                    }

                    return@map if (changed) {
                        uuid to serverObject.value.first
                    } else {
                        // not changed
                        null
                    }
                }
                // skip not changed objects
                .filterNotNull()

                // removed objects
                .plus(clientGameData.objectCacheList.filter { !jsonGameObjects.containsKey(it.key) }
                    .map { it.key to null })
                .toMap()
        }


        if (clientChangedObjects.isNotEmpty() || clientGameData.forcedUpdate || clientGameData.clientSidePlayerHashCode != clientGameData.entityPlayer.hashCode()) {
            val sendObjectsList = SendObjectsList(
                clientChangedObjects, NewGsonGameObject(clientGameData.entityPlayer), teleport = !clientGameData.entityPlayer.location.approx(clientGameData.clientSideLocation)
            )

            clientPlayer.sendObject(sendObjectsList.transport())

            // Update hash to avoid redundant reuploads
            // Assume the client will receive it
            clientGameData.objectCacheList.clear()
            clientGameData.objectCacheList.putAll(jsonGameObjects.map { it.key to it.value.value.second })
        }
        clientGameData.forcedUpdate = false
        clientGameData.clientSidePlayerHashCode = clientGameData.entityPlayer.hashCode()
    }
}