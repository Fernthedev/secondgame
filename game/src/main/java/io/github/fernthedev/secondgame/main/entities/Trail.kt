package io.github.fernthedev.secondgame.main.entities

import com.github.fernthedev.universal.EntityID
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.Location
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.logic.IEntityRenderer
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Graphics2D

class Trail(
    location: Location,
    width: Float,
    height: Float,

    color: Color,

    private val life: Float = 0f
) : GameObject(
    color = color,
    location = location,
    width = width,
    height = height,
    entityId = EntityID.TRAIL
), Cloneable {
    var alpha: Float = 1f
    private set

    init {
        hasTrail = false
    }


    override fun tick() {
        checkTrail(this)
        if (alpha > life) {
            alpha -= life - 0.0001f
        } else {
            Game.staticEntityRegistry.removeEntityObject(this)
        }
    }


    class Renderer : IEntityRenderer<Trail> {
        override fun render(g: Graphics2D, gameObject: Trail, drawX: Float, drawY: Float) {
            val g2d: Graphics2D = g

//            checkTrail(gameObject);
            g2d.composite = makeTransparent(gameObject.alpha)
            g.color = gameObject.color
            g.fillRect(drawX.toInt(), drawY.toInt(), gameObject.width.toInt(), gameObject.height.toInt())
            g2d.composite = makeTransparent(1f)
        }
    }

    companion object {
        private fun makeTransparent(alpha: Float): AlphaComposite? {
            val type: Int = AlphaComposite.SRC_OVER
            try {
                return AlphaComposite.getInstance(type, alpha)
            } catch (e: IllegalArgumentException) {
                Game.loggerImpl.info((alpha >= 0.0f).toString() + " " + (alpha <= 1.0f) + " " + alpha)
                e.printStackTrace()
            }
            return null
        }

        @Throws(IllegalArgumentException::class)
        private fun checkTrail(gameObject: Trail) {
            val values: MutableList<String> = ArrayList()
            if (gameObject.height == 0f) values.add("height")
            if (gameObject.width == 0f) values.add("width")
            if (gameObject.life <= 0.0) values.add("life " + gameObject.life)
            if (gameObject.alpha >= 1.0001) values.add("alpha " + gameObject.alpha)
            require(values.isEmpty()) { "These values are incorrect: $values" }
        }
    }
}