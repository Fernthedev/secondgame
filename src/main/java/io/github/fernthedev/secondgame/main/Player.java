//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.*;
import java.util.Random;

public class Player extends GameObject {

    Random r = new Random();
    Handler handler;
    HUD hud;

    public Player(int x, int y, ID id, Handler handler,HUD hud) {
        super(x, y, id);
        this.handler = handler;
        this.hud = hud;
    }

    public void tick() {
        x += velX;
        y += velY;
        x = Game.clamp(x,0,Game.WIDTH - 37);
        y = Game.clamp(y,0,Game.HEIGHT - 60);
        handler.addObject(new Trail(x,y,ID.Trail,Color.WHITE,32,32,0.05f,handler));
        collision();
    }

    private void collision() {
        for(int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.BasicEnemey || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy || tempObject.getId() == ID.EnemyBoss) {
                if(getBounds().intersects(tempObject.getBounds())) {
                    //COLLISION CODE
                    HUD.HEALTH -= 2;
                }
            }

            if (tempObject.getId() == ID.Coin) {
                if (getBounds().intersects(tempObject.getBounds())){
                    HUD.coin++;
                    this.handler.removeObject(tempObject);
                }
            }
        }

    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, 32, 32);

    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,32,32);
    }
}
