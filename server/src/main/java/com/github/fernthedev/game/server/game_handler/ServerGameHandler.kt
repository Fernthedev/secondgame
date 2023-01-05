package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.TickRunnable
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.game.server.NewServerEntityRegistry
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.universal.entity.EntityPlayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ServerGameHandler(
    val entityHandler: NewServerEntityRegistry,
    val spawn: Spawn,
    val server: GameServer
) : TickRunnable {
    var started = false

    override suspend fun tick(): Unit = coroutineScope {
        val spawnJob = launch {
            spawn.tick()
        }

        val entityJob = launch {
            entityHandler.tick()
        }

        entityJob.join()
        spawnJob.join()

        if (started && !entityHandler.gameObjects
                .any { (_, gameObject) -> gameObject is EntityPlayer }
        ) {
            started = false
            if (server.server.playerHandler.channelMap.isNotEmpty()) {
                server.server.playerHandler.channelMap.values.forEach { connection ->
                    try {
                        connection.sendObject(GameOverPacket()).sync()
                    } catch (e: InterruptedException) {
                        Thread.currentThread().interrupt()
                        e.printStackTrace()
                    }
                    connection.close()
                }

                entityHandler.gameObjects.clear()
                entityHandler.clientGameDataMap.clear()
            }
        }
    }
}