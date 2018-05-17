package io.github.fernthedev.secondgame.main;

import java.awt.*;

public class SmartEnemy extends GameObject {

    private Handler handler;
    private GameObject player;


    public SmartEnemy(float x, float y, ID id, Handler handler) {
        super(x, y, id);

        this.handler = handler;

        for(int i = 0; i < handler.object.size();i++) {
            GameObject tempObject = handler.object.get(i);
            if(tempObject.id == ID.Player) player = tempObject;
        }

    }

    public void tick() {
        x +=velX;
        y +=velY;

        float diffX = x - player.getX() - 16;
        float diffY = y - player.getY() - 16;
        float distance = (float) Math.sqrt( (x-player.getX()) * (x-player.getX()) + (y-player.getY()) * (y-player.getY()) );

        velX = ((-1/distance)* diffX);
        velY = ((-1/distance)* diffY);

        //if(x <= 0 || x >= Game.WIDTH - 16)  velX *= -1;
        //if(y <= 0 || y >= Game.HEIGHT - 32)  velY *= -1;

        handler.addObject(new Trail(x,y,ID.Trail,Color.green,16,16,0.02f,handler));

    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int)x,(int)y,16,16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}
