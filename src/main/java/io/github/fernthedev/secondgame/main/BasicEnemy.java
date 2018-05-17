//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BasicEnemy extends GameObject {
    private Handler handler;

    public BasicEnemy(int x, int y, ID id, Handler handler) {
        super((float)x, (float)y, id);
        this.handler = handler;
        this.velX = 5.0F;
        this.velY = 5.0F;
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
        if (this.x <= 0.0F || this.x >= 624.0F) {
            this.velX *= -1.0F;
        }

        if (this.y <= 0.0F || this.y >= 445.0F) {
            this.velY *= -1.0F;
        }

        this.handler.addObject(new Trail(this.x, this.y, ID.Trail, Color.RED, 16, 16, 0.02F, this.handler));
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)this.x, (int)this.y, 16, 16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 16, 16);
    }
}
