//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package io.github.fernthedev.secondgame.main

import com.github.fernthedev.universal.Location
import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.BasicEnemy
import com.github.fernthedev.universal.entity.Coin
import com.github.fernthedev.universal.entity.FastEnemy
import com.github.fernthedev.universal.entity.SmartEnemy
import io.github.fernthedev.secondgame.main.logic.NewClientEntityRegistry

internal class Spawn(private val hud: HUD, private val game: Game) {
    private var scoreKeep = 0
    private val r = UniversalHandler.RANDOM
    private var timer = 0
    private var nexttimer = 0
    fun tick() {
        val handler: NewClientEntityRegistry = Game.staticEntityRegistry
        scoreKeep++
        val coinspawn = hud.score + r.nextInt(512)
        if (hud.score == coinspawn) {
            Game.staticEntityRegistry.addEntityObject(
                Coin(Location(r.nextFloat(UniversalHandler.WIDTH - 50f), r.nextFloat(UniversalHandler.HEIGHT - 50f)))
            )
            //handler.addObject(new Coin(r.nextInt(GAME.WIDTH - 50), r.nextInt(GAME.HEIGHT - 50), EntityID.Coin, GameObject.entities));
        }
        if (scoreKeep >= 250) {
            hud.level = hud.level + 1
            timer++
            scoreKeep = 0
            var mob = r.nextInt(4)
            if (mob == 0) mob++

            //System.out.println(mob);
            if (mob == 1) {
                handler.addEntityObject(
                    BasicEnemy(
                        Location(
                            r.nextFloat(UniversalHandler.WIDTH - 50f),
                            r.nextFloat(UniversalHandler.HEIGHT - 50f),
                        )
                    )
                )
            }
            if (mob == 2) {
                handler.addEntityObject(
                    FastEnemy(
                        Location(
                            r.nextFloat(UniversalHandler.WIDTH - 50f),
                            r.nextFloat(UniversalHandler.HEIGHT - 50f),
                        )
                    )
                )
            }
            if (mob == 3) {
                handler.addEntityObject(
                    SmartEnemy(
                        Location(
                            r.nextFloat(UniversalHandler.WIDTH - 50f),
                            r.nextFloat(UniversalHandler.HEIGHT - 50f),
                        ),
                        Game.mainPlayer!!.uniqueId
                    )
                )
            }
        }
        if (timer >= nexttimer) {
            val min = 7
            val max = 15
            nexttimer = r.nextInt(max - min) + min
            timer = 0
            handler.resetLevel()
            scoreKeep = 250
            hud.level = hud.level - 1
        }
    }
}