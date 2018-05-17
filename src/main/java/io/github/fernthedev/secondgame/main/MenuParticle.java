//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class MenuParticle extends GameObject {
    private Handler handler;
    private Random r = new Random();
    private Color col;
    int dir = 0;

    public MenuParticle(int x, int y, ID id, Handler handler) {
        super((float)x, (float)y, id);
        this.handler = handler;
        this.dir = this.r.nextInt(2);
        this.velX = (float)(this.r.nextInt(14) + -7);
        this.velY = (float)(this.r.nextInt(14) + -7);
        if (this.velX == 0.0F) {
            this.velX = 1.0F;
        }

        if (this.velY == 0.0F) {
            this.velY = 1.0F;
        }

        this.col = new Color(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255));
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

        this.handler.addObject(new Trail(this.x, this.y, ID.Trail, this.col, 16, 16, 0.05F, this.handler));
    }

    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int)this.x, (int)this.y, 16, 16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 16, 16);
    }
}
