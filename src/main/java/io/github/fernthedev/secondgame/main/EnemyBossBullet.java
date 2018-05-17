//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class EnemyBossBullet extends GameObject {
    private Handler handler;
    private Random r = new Random();

    public EnemyBossBullet(int x, int y, ID id, Handler handler) {
        super((float)x, (float)y, id);
        this.handler = handler;
        this.velX = (float)(this.r.nextInt(10) + -5);
        this.velY = 5.0F;
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
        if (this.y >= 477.0F) {
            this.handler.removeObject(this);
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
