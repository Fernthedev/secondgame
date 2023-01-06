//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package io.github.fernthedev.secondgame.main


import io.github.fernthedev.secondgame.main.ui.api.ScreenFont
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.util.concurrent.TimeUnit

class HUD {
    private var greenvalue = 255
    var score = 0
    var level = 1

    val textFont: ScreenFont = ScreenFont(Font("arial", Font.PLAIN, 12), Color.WHITE)
    fun tick() {
        if (Game.mainPlayer == null) return

        greenvalue = Game.clamp(greenvalue.toFloat(), 0.0f, 255.0f).toInt()
        greenvalue = Game.mainPlayer!!.health * 2
        score++
    }

    fun render(g: Graphics2D) {
        g.font = textFont.font

        val mainPlayer = Game.mainPlayer

        if (mainPlayer != null) {
            g.color = Color.GRAY
            g.fillRect(15, 15, 200, 32)

            g.color = Color(75, greenvalue, 0)
            g.fillRect(15, 15, mainPlayer.health * 2, 32)

            if (mainPlayer.health == 0) {
                g.color = Color.RED
                g.drawRect(15, 15, 200, 32)
            } else {
                g.color = Color.WHITE
                g.drawRect(15, 15, 200, 32)
            }

            g.color = Color.WHITE
            g.drawString("Coins: " + mainPlayer.coin, 15, 96)
        }
        
        g.color = Color.WHITE
        g.drawString("Score: $score", 15, 64)
        g.drawString("Level: $level", 15, 80)

        if (Game.client != null) {
            try {
                g.drawString("Ping: " + Game.client!!.getPingTime(TimeUnit.MILLISECONDS) + "ms", 15, 112)
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
    }
}