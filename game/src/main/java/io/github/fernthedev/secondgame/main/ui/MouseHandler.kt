package io.github.fernthedev.secondgame.main.ui

import com.github.fernthedev.lightchat.core.StaticHandler
import com.github.fernthedev.universal.entity.EntityPlayer
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.api.Screen
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class MouseHandler : MouseAdapter() {
    /**
     * {@inheritDoc}
     *
     * @param e
     */
    override fun mousePressed(e: MouseEvent) {
        val mx = e.x
        val my = e.y
        if (Game.screen != null) {
            val screen: Screen = Game.screen!!
            screen.buttonList.parallelStream().forEach { screenButton ->
                if (mouseOver(mx, my, screenButton)) {
                    screenButton.onClick.run()
                }
            }
        } else {
            // Debug tool
            if (Game.mainPlayer != null && StaticHandler.isDebug()) {
                val entityPlayer: EntityPlayer = Game.mainPlayer!!
                entityPlayer.location.x = mx.toFloat()
                entityPlayer.location.y = my.toFloat()
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    override fun mouseReleased(e: MouseEvent) {}
    private fun mouseOver(mx: Int, my: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
        return mx > x && mx < x + width && my > y && my < y + height
    }

    private fun mouseOver(mx: Int, my: Int, ui: ScreenButton): Boolean {
        return mx > ui.renderedLocation.x && mx < ui.renderedLocation.x + ui.buttonSize.width && my > ui.renderedLocation.y && my < ui.renderedLocation.y + ui.buttonSize.height
    }
}