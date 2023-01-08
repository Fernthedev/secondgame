//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package io.github.fernthedev.secondgame.main.entities

import com.github.fernthedev.universal.entity.Coin
import io.github.fernthedev.secondgame.main.BufferedImageLoader
import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.logic.IEntityRenderer
import java.awt.Graphics2D
import java.awt.Image

class CoinRenderer : IEntityRenderer<Coin> {
    private val coin_image: Image

    init {
        val loader = BufferedImageLoader()
        coin_image = loader.loadImage("/icon.png")
    }

    /*
    @Deprecated
    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }*/
    override fun render(g: Graphics2D, gameObject: Coin, drawX: Float, drawY: Float) {
        if (Game.`fern$` <= 10) {

//            g.setColor(color);
//            g.fillRect((int)gameObject.getX(), (int)gameObject.getY(), 0, 0);
            g.drawImage(
                coin_image,
                drawX.toInt(),
                drawY.toInt(),
                gameObject.width.toInt(),
                gameObject.height.toInt(),
                null
            )
        } else {
            g.color = gameObject.color
            g.fillRect(drawX.toInt(), drawY.toInt(), gameObject.width.toInt(), gameObject.height.toInt())
        }
    }
}