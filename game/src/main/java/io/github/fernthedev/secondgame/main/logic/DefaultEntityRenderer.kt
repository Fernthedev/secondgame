package io.github.fernthedev.secondgame.main.logic

import com.github.fernthedev.universal.GameObject
import java.awt.Graphics2D

class DefaultEntityRenderer : IEntityRenderer<GameObject> {
    override fun render(g: Graphics2D, gameObject: GameObject, drawX: Float, drawY: Float) {
        g.color = gameObject.color
        g.fillRect(drawX.toInt(), drawY.toInt(), gameObject.width.toInt(), gameObject.height.toInt())
    }
}