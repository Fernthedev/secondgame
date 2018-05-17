package io.github.fernthedev.secondgame.main;

import java.awt.*;
import java.util.Random;

public class MenuParticle extends GameObject {

    private Handler handler;
    private Random r = new Random();

    private Color col;

    int dir = 0;


    public MenuParticle(int x, int y, ID id, Handler handler) {
        super(x, y, id);

        this.handler = handler;


        dir = r.nextInt(2);

        velX =(r.nextInt(7 - -7) + -7);
        velY =(r.nextInt(7 - -7) + -7);

        if(velX == 0) velX = 1;
        if(velY == 0) velY = 1;

        col = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
    }

    public void tick() {
        x +=velX;
        y +=velY;

        if(x <= 0 || x >= Game.WIDTH - 16)  velX *= -1;
        if(y <= 0 || y >= Game.HEIGHT - 32)  velY *= -1;

        handler.addObject(new Trail(x,y,ID.Trail,col,16,16,0.05f,handler));

    }

    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int)x,(int)y,16,16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}
