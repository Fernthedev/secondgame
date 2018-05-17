//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Coin extends GameObject {
    private Handler handler;
    private BufferedImage coin_image;

    public Coin(int x, int y, ID id, Handler handler) {
        super((float)x, (float)y, id);
        this.handler = handler;
        this.velX = 0.0F;
        this.velY = 0.0F;
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
    }

    public void render(Graphics g) {
        if (Game.fern$ <= 10) {
            BufferedImageLoader loader = new BufferedImageLoader();
            this.coin_image = loader.loadImage("/icon.png");
            g.setColor(Color.ORANGE);
            g.fillRect((int)this.x, (int)this.y, 0, 0);
            g.drawImage(this.coin_image, (int)this.x, (int)this.y, (ImageObserver)null);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect((int)this.x, (int)this.y, 16, 16);
        }

    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 16, 16);
    }
}
