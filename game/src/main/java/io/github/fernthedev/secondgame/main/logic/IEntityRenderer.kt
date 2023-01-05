package io.github.fernthedev.secondgame.main.logic

import com.github.fernthedev.universal.GameObject
import java.awt.Graphics2D

interface IEntityRenderer<T : GameObject?> {
    fun render(g: Graphics2D, gameObject: T, drawX: Float, drawY: Float)
}