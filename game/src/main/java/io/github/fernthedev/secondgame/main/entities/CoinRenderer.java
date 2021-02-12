//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.entity.Coin;
import io.github.fernthedev.secondgame.main.BufferedImageLoader;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.logic.IEntityRenderer;

import java.awt.*;

public class CoinRenderer implements IEntityRenderer<Coin>  {

    private Image coin_image;

    public CoinRenderer() {
        BufferedImageLoader loader = new BufferedImageLoader();
        coin_image = loader.loadImage("/icon.png");
    }

    /*
    @Deprecated
    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }*/

    @Override
    public void render(Graphics g, Coin gameObject, float drawX, float drawY) {
        if (Game.fern$ <= 10) {

//            g.setColor(color);
//            g.fillRect((int)gameObject.getX(), (int)gameObject.getY(), 0, 0);
            g.drawImage(coin_image, (int)drawX, (int)drawY, gameObject.getWidth(), gameObject.getHeight(), null);
        } else {
            g.setColor(gameObject.getColor());
            g.fillRect((int)drawX, (int)drawY, gameObject.getWidth(), gameObject.getHeight());
        }
    }


}
