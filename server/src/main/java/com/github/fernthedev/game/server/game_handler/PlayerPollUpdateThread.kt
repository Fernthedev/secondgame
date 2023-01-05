package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.game.server.ClientGameData
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.game.server.NewServerEntityRegistry
import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.lightchat.server.ClientConnection
import com.github.fernthedev.packets.object_updates.SendObjectsList
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.entity.EntityPlayer
import com.github.fernthedev.universal.entity.NewGsonGameObject
import com.google.common.base.Stopwatch
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This thread watches for updates
 * on the server and sends player updates
 *
 */
class PlayerPollUpdateThread(private val gameServer: GameServer) : Thread() {
    val sendUpdate: AtomicBoolean = AtomicBoolean(false)
    private val updateStopwatch = Stopwatch.createStarted()



    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see Thread.run
     */
    override fun run() {
        while (gameServer.server.isRunning) {
            if (sendUpdate.get()) {
                sendUpdate.set(false)
                update()
                try {
                    // TODO: Play around with value
                    sleep(100)
                } catch (e: InterruptedException) {
                    interrupt()
                    e.printStackTrace()
                }
            }
            try {
                sleep(10)
            } catch (e: InterruptedException) {
                interrupt()
                e.printStackTrace()
            }
        }
    }

    private fun update() {
        val entityHandler: NewServerEntityRegistry = gameServer.serverGameHandler.entityHandler
        val oldHashCode: Int = entityHandler.gameObjects.hashCode()
        val stopWatch = Stopwatch.createStarted()
        val clientConnections = entityHandler.clientGameDataMap.keys

        val completableFutures: MutableList<CompletableFuture<Void>> = ArrayList()

        val changed = AtomicBoolean(false)

        for (clientConnection in clientConnections) {
            completableFutures.add(
                CompletableFuture.runAsync(
                    { changed.set(updatePlayerInfo(clientConnection) || changed.get()) },
                    gameServer.server.executorService
                )
            )
        }
        try {
            CompletableFuture.allOf(*completableFutures.toTypedArray<CompletableFuture<*>>()).thenRun(Runnable {
                stopWatch.stop()
                if (oldHashCode != entityHandler.gameObjects.hashCode()) {
                    StaticHandler.getCore().logger.debug(
                        "Updating players took {}", stopWatch.elapsed(
                            TimeUnit.MILLISECONDS
                        )
                    )
                }
                if (changed.get()) {
                    StaticHandler.getCore().logger
                        .debug("{}ms since last update", updateStopwatch.elapsed(TimeUnit.MILLISECONDS))
                    updateStopwatch.reset()
                    updateStopwatch.start()
                }
            }).get(30, TimeUnit.SECONDS)

        } catch (e: InterruptedException) {
            currentThread().interrupt()
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            gameServer.server.logger.error("An update took more than 30 seconds to complete", e)
        }
    }

    /**
     *
     * @param clientPlayer
     * @return true if sent an update
     */
    private fun updatePlayerInfo(clientPlayer: ClientConnection): Boolean {
        val newGameObjects: MutableMap<UUID, NewGsonGameObject> = HashMap()
        val clientGameData: ClientGameData = gameServer.entityRegistry.clientGameDataMap[clientPlayer]
            ?: throw NullPointerException("ClientGameData is null ")

        val cachedObjectMap: Map<UUID, GameObject> = gameServer.entityRegistry.gameObjects

        cachedObjectMap.forEach { (uuid: UUID, gameObject: GameObject) ->
            var changed = !clientGameData.objectCacheList.containsKey(uuid) ||
                    clientGameData.objectCacheList[uuid] != null &&
                    clientGameData.objectCacheList[uuid] != gameObject.hashCode()
            if (changed && uuid === clientGameData.entityPlayer.uniqueId) {
                changed = clientGameData.clientSidePlayerHashCode != clientGameData.entityPlayer.hashCode() ||
                        EntityPlayer.isPlayerDifferent(
                            clientGameData.entityPlayer,
                            gameObject as EntityPlayer,
                            0.0F,
                            0.0F
                        )
            }
            if (changed) {
                newGameObjects[uuid] = NewGsonGameObject(gameObject)
            }
        }
        try {
            HashMap(clientGameData.objectCacheList).forEach { (uuid, _) ->
                if (!cachedObjectMap.containsKey(uuid)) {
                    newGameObjects[uuid] = NewGsonGameObject.nullObject()
                }
            }
        } catch (e: ConcurrentModificationException) {
            e.printStackTrace()
            return false
        }
        val sendObjectsList = SendObjectsList(
            newGameObjects, clientGameData.entityPlayer
        )

        var result = false
        if (newGameObjects.isNotEmpty() || clientGameData.clientSidePlayerHashCode != clientGameData.entityPlayer.hashCode()) {
            clientPlayer.sendObject(sendObjectsList)
            result = true
        }
        clientGameData.clientSidePlayerHashCode = clientGameData.entityPlayer.hashCode()
        return result
    }
}