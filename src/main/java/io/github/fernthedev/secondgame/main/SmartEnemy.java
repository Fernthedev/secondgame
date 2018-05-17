//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class SmartEnemy extends GameObject {
    private Handler handler;
    private GameObject player;

    public SmartEnemy(float x, float y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;

        for(int i = 0; i < handler.object.size(); ++i) {
            GameObject tempObject = (GameObject)handler.object.get(i);
            if (tempObject.id == ID.Player) {
                this.player = tempObject;
            }
        }

    }

    public void tick() {
        this.x += this.velX;
        this.y += this.velY;
        float diffX = this.x - this.player.getX() - 16.0F;
        float diffY = this.y - this.player.getY() - 16.0F;
        float distance = (float)Math.sqrt((double)((this.x - this.player.getX()) * (this.x - this.player.getX()) + (this.y - this.player.getY()) * (this.y - this.player.getY())));
        this.velX = -1.0F / distance * diffX;
        this.velY = -1.0F / distance * diffY;
        this.handler.addObject(new Trail(this.x, this.y, ID.Trail, Color.green, 16, 16, 0.02F, this.handler));
    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int)this.x, (int)this.y, 16, 16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)this.x, (int)this.y, 16, 16);
    }
}
