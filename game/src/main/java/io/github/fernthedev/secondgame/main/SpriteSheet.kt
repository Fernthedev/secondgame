package io.github.fernthedev.secondgame.main

import java.awt.image.BufferedImage

internal class SpriteSheet(private val sprite: BufferedImage) {
    fun grapImage(col: Int, row: Int, width: Int, height: Int): BufferedImage {
        return sprite.getSubimage(row * 32 - 32, col * 32 - 32, width, height)
    }
}