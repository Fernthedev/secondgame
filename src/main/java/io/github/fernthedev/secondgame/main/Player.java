//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Player extends GameObject {
    Random r = new Random();
    Handler handler;
    HUD hud;

    public Player(int x, int y, ID id, Handler handler, HUD hud) {
        super((float)x, (float)y, id);
        this.handler = handler;
        new SpriteSheet(Game.sprite_sheet);
    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
        this.x = Game.clamp(this.x, 0.0F, 603.0F);
        this.y = Game.clamp(this.y, 0.0F, 417.0F);
        this.handler.addObject(new Trail(this.x, this.y, ID.Trail, Color.WHITE, 32, 32, 0.05F, this.handler));
        this.collision();
    }

    private void collision() {
        for(int i = 0; i < this.handler.object.size(); ++i) {
            GameObject tempObject = (GameObject)this.handler.object.get(i);
            if ((tempObject.getId() == ID.BasicEnemey || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy || tempObject.getId() == ID.EnemyBoss) && this.getBounds().intersects(tempObject.getBounds())) {
                HUD.HEALTH -= 2;
            }

            if (tempObject.getId() == ID.Coin && this.getBounds().intersects(tempObject.getBounds())) {
                ++HUD.coin;
                this.handler.removeObject(tempObject);
            }
        }

    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)this.x, (int)this.y, 32, 32);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 32, 32);
    }
}
