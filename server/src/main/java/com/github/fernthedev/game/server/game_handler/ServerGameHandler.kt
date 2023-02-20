package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.TickRunnable
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.game.server.NewServerEntityRegistry
import com.github.fernthedev.lightchat.core.encryption.transport
import com.github.fernthedev.packets.GameOverPacket
import com.github.fernthedev.universal.entity.EntityPlayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class ServerGameHandler(
    val server: GameServer,
    val entityHandler: NewServerEntityRegistry,
    val spawn: Spawn,
    val playerUpdateHandler: PlayerUpdateHandler
) : TickRunnable {
    var started = false

    override suspend fun tick(): Unit = coroutineScope {
        val spawnJob = launch {
            spawn.tick()
        }

        val entityJob = launch {
            entityHandler.tick()
        }

        val playerJob = launch {
            playerUpdateHandler.update()
        }

        entityJob.join()
        spawnJob.join()
        playerJob.join()

        if (!started || entityHandler.gameObjects
                .any { (_, gameObject) -> gameObject is EntityPlayer }
        ) {
            return@coroutineScope
        }


        started = false
        if (server.server.playerHandler.channelMap.isEmpty()) {
            return@coroutineScope
        }

        entityHandler.clearEntities()
        entityHandler.clientGameDataMap.clear()

        val packet = GameOverPacket().transport()
        server.server.playerHandler.channelMap.values.map { connection ->
            connection to connection.sendPacketDeferred(packet)
        }.forEach { (connection, deferred) ->
            val future = deferred.await()

            while (!future.isDone) {
                yield()
            }

            connection.close()
        }
    }
}