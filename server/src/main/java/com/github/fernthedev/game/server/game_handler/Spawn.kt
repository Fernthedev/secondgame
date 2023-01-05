package com.github.fernthedev.game.server.game_handler

import com.github.fernthedev.TickRunnable
import com.github.fernthedev.game.server.GameServer
import com.github.fernthedev.packets.LevelUp
import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.*


class Spawn(
     val server: GameServer
) : TickRunnable {
    private var scoreKeep = 0
    private var timer = 0
    private var nextTimer = 0
    private var levels = 0;

    override suspend fun tick() {
        if (!server.serverGameHandler.started) return

        if (server.serverGameHandler.entityHandler.isClientDataEmpty) {
            server.serverGameHandler.entityHandler.gameObjects.clear()
            return
        }
        scoreKeep++
        val coinSpawn = scoreKeep + UniversalHandler.RANDOM.nextInt(512)
        if (scoreKeep == coinSpawn && server.server.playerHandler.channelMap.isNotEmpty()) {
            server.serverGameHandler.entityHandler.addEntityObject(
                Coin(
                    Location(
                        UniversalHandler.RANDOM.nextFloat(UniversalHandler.WIDTH - 50F) + 1,
                        UniversalHandler.RANDOM.nextFloat(UniversalHandler.HEIGHT - 50F) + 1
                    ),
                    EntityID.COIN
                )
            )
        }
        if (scoreKeep >= 250) {
            timer++
            scoreKeep = 0
            if (server.server.playerHandler.channelMap.isNotEmpty()) {
                var mob = UniversalHandler.RANDOM.nextInt(4)
                if (mob == 0) mob++


                if (mob == 1) {
                    server.serverGameHandler.entityHandler.addEntityObject(
                        BasicEnemy(
                            Location(
                                UniversalHandler.RANDOM.nextFloat(UniversalHandler.WIDTH - 50f),
                                UniversalHandler.RANDOM.nextFloat(UniversalHandler.HEIGHT - 50f)
                            ),
                            EntityID.ENEMY
                        )
                    )
                }
                if (mob == 2) {
                    server.serverGameHandler.entityHandler.addEntityObject(
                        FastEnemy(
                            Location(
                                UniversalHandler.RANDOM.nextFloat(UniversalHandler.WIDTH - 50F),
                                UniversalHandler.RANDOM.nextFloat(UniversalHandler.HEIGHT - 50F)
                            ),
                            EntityID.ENEMY
                        )
                    )
                }
                if (mob == 3) {
                    server.serverGameHandler.entityHandler.addEntityObject(
                        SmartEnemy(
                            Location(
                                UniversalHandler.RANDOM.nextFloat(UniversalHandler.WIDTH - 50F),
                                UniversalHandler.RANDOM.nextFloat(UniversalHandler.HEIGHT - 50F)
                            ),
                            playerUUID = server.serverGameHandler.entityHandler.gameObjects.values
                                .parallelStream()
                                .filter { gameObject -> gameObject is EntityPlayer }
                                .findAny()
                                .map { gameObject -> gameObject as EntityPlayer? }
                                .get().uniqueId
                        )
                    )
                }

            }
            if (timer >= nextTimer) {
                val min = 7
                val max = 15
                nextTimer = UniversalHandler.RANDOM.nextInt(max - min) + min
                timer = 0

                server.serverGameHandler.entityHandler.removeRespawnAllPlayers()
                levels++

                server.server.sendObjectToAllPlayers(LevelUp(levels))
                scoreKeep = 200
            }
        }
    }
}