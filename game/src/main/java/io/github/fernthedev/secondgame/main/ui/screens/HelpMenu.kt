package io.github.fernthedev.secondgame.main.ui.screens

import io.github.fernthedev.secondgame.main.ui.api.Screen
import java.awt.Graphics2D

class HelpMenu : Screen(TITLE) {
    init {
        Screen.addMenuParticles()
    }

    override fun draw(g: Graphics2D) {
        g.font = textFont.font
        g.color = textFont.color
        g.drawString("Use WASD or arrow keys please", buttonX(DEFAULT_BACK_BUTTON) - 90, buttonYStart() + 130)
        addButton(DEFAULT_BACK_BUTTON)
    }

    companion object {
        private const val TITLE = "HELP"
    }
}