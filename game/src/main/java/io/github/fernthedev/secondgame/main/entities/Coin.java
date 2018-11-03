//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.entity.UniversalCoin;
import io.github.fernthedev.secondgame.main.BufferedImageLoader;
import io.github.fernthedev.secondgame.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends UniversalCoin {

    public Coin(int x, int y, ID id, int objectID) {
        super(x, y, id,objectID);
        this.velX = 0.0F;
        this.velY = 0.0F;
    }

    public Coin(GameObject gameObject) {
        super(gameObject);
    }

    /*
    @Deprecated
    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }*/

    @Override
    public void render(Graphics g) {
        if (Game.fern$ <= 10) {
            BufferedImageLoader loader = new BufferedImageLoader();
            BufferedImage coin_image = loader.loadImage("/icon.png");
            g.setColor(Color.ORANGE);
            g.fillRect((int)this.x, (int)this.y, 0, 0);
            g.drawImage(coin_image, (int)this.x, (int)this.y, null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect((int)this.x, (int)this.y, 16, 16);
        }

    }


}
