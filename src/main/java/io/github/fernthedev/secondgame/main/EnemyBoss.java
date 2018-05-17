//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class EnemyBoss extends GameObject {
    private Handler handler;
    private Random r = new Random();
    private int timer = 60;
    private int timer2 = 50;

    public EnemyBoss(int x, int y, ID id, Handler handler) {
        super((float)x, (float)y, id);
        this.handler = handler;
        this.velX = 0.0F;
        this.velY = 2.0F;
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
        if (this.timer <= 0) {
            this.setVelY(0);
        } else {
            --this.timer;
        }

        if (this.timer <= 0) {
            --this.timer2;
        }

        if (this.timer2 <= 0) {
            if (this.velX == 0.0F) {
                this.velX = 2.0F;
            }

            if (this.velX > 0.0F) {
                this.velX += 0.005F;
            } else if (this.velX < 0.0F) {
                this.velX -= 0.005F;
            }

            this.velX = Game.clamp(this.velX, -10.0F, 10.0F);
            int spawn = this.r.nextInt(10);
            if (spawn == 0) {
                this.handler.addObject(new EnemyBossBullet((int)this.x + 48, (int)this.y + 48, ID.BasicEnemey, this.handler));
            }
        }

        if (this.x <= 0.0F || this.x >= 544.0F) {
            this.velX *= -1.0F;
        }

    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)this.x, (int)this.y, 96, 96);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 16, 16);
    }
}
