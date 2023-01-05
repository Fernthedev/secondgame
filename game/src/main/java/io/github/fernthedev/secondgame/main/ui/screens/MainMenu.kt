package io.github.fernthedev.secondgame.main.ui.screens

import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.api.Screen
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton
import java.awt.Graphics2D
import kotlin.system.exitProcess

class MainMenu : Screen(TITLE) {
    init {
        addMenuParticles()
    }

    override fun draw(g: Graphics2D) {
        addButton(ScreenButton("Multiplayer") { Game.screen = MultiplayerScreen() })
        addButton(ScreenButton("Play") { Game.staticEntityRegistry.startGame() })
        addButton(ScreenButton("Help") { Game.screen = HelpMenu() })
        addButton(DEFAULT_BACK_BUTTON)
    }

    override fun returnToParentScreen() {
        exitProcess(0)
    }

    companion object {
        private const val TITLE = "MENU"
    }
}