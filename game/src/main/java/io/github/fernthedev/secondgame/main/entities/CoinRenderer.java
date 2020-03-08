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
import java.awt.image.BufferedImage;

public class CoinRenderer implements IEntityRenderer<Coin>  {


    /*
    @Deprecated
    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }*/

    @Override
    public void render(Graphics g, Coin gameObject) {
        if (Game.fern$ <= 10) {
            BufferedImageLoader loader = new BufferedImageLoader();
            BufferedImage coin_image = loader.loadImage("/icon.png");
//            g.setColor(color);
//            g.fillRect((int)gameObject.getX(), (int)gameObject.getY(), 0, 0);
            g.drawImage(coin_image, (int)gameObject.getX(), (int)gameObject.getY(), null);
        } else {
            g.setColor(gameObject.getColor());
            g.fillRect((int)gameObject.getX(), (int)gameObject.getY(), gameObject.getWidth(), gameObject.getHeight());
        }
    }


}
