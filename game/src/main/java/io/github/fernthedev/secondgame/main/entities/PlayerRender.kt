package io.github.fernthedev.secondgame.main.entities

import com.github.fernthedev.universal.UniversalHandler
import com.github.fernthedev.universal.entity.EntityPlayer
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.logic.IEntityRenderer
import io.github.fernthedev.secondgame.main.ui.api.ScreenFont
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

class PlayerRender : IEntityRenderer<EntityPlayer> {
    protected var textFont = ScreenFont(Font("arial", Font.BOLD, 15), Color.WHITE)
    override fun render(g: Graphics2D, gameObject: EntityPlayer, drawX: Float, drawY: Float) {
//        float drawX = gameObject.getX() + (gameObject.getX() - gameObject.getPrevX()) * Game.getRenderTime();
//        float drawY = gameObject.getY() + (gameObject.getY() - gameObject.getPrevY()) * Game.getRenderTime();
        g.color = gameObject.color
        g.fillRect(drawX.toInt(), drawY.toInt(), gameObject.width.toInt(), gameObject.height.toInt())
        if (Game.client?.isRegistered == true) {
            var y = (drawY - gameObject.height / 4f).toInt()
            val maxY = UniversalHandler.HEIGHT.toFloat() - gameObject.height * 2f
            val outOfBoundsRoof = y - gameObject.height <= 0
            val outOfBoundsFloor = y + gameObject.height >= maxY
            if ((outOfBoundsRoof || gameObject.velY > 2) && !outOfBoundsFloor) y =
                (drawY + gameObject.height * 1.5).toInt()
            g.font = textFont.font
            g.drawString(gameObject.name, gameObject.location.x.toInt(), y)
        }
    }
}