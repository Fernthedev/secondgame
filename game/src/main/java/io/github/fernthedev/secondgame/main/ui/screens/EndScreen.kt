package io.github.fernthedev.secondgame.main.ui.screens

import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.HUD
import io.github.fernthedev.secondgame.main.ui.api.Screen
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton
import java.awt.Color
import java.awt.Graphics2D

class EndScreen : Screen(TITLE) {
    init {
        addMenuParticles()
        setParentScreenOnSet = false
        parentScreen = MainMenu()
    }

    override fun draw(g: Graphics2D) {
        val hud: HUD = Game.hud
        g.color = Color.WHITE
        g.drawString("Score: " + hud.score, 150, incrementY(-buttonSpacing + 30))
        g.drawString("Level: " + hud.level, 150, incrementY(-buttonSpacing + 30))
        g.drawString("Coin: " + Game.mainPlayer!!.coin, 150, incrementY(-buttonSpacing + 30))
        addButton(ScreenButton("Try Again") {
            hud.level = 1
            hud.score = 0
            Game.screen = MainMenu()
        })
    }

    companion object {
        private const val TITLE = "GAME Over"
    }
}