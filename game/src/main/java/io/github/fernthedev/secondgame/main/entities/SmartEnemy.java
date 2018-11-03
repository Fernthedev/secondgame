package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.Trail;
import io.github.fernthedev.secondgame.main.Game;

import java.awt.*;

public class SmartEnemy extends GameObject {


    private GameObject player;


    public SmartEnemy(float x, float y, ID id, int objectID,GameObject playerE) {
        super(x, y, id,objectID);
        this.player = playerE;

        for(int i = 0; i < UniversalHandler.getThingHandler().getGameObjects().size(); i++) {
            GameObject tempObject = UniversalHandler.getThingHandler().getGameObjects().get(i);
            if(tempObject.id == ID.Player) player = tempObject;
        }

    }

    public SmartEnemy(GameObject gameObject) {
        super(gameObject);
    }

    public void tick() {
        x +=velX;
        y +=velY;

        player = Game.mainPlayer;

        if(player != null) {

            float diffX = x - player.getX() - 16;
            float diffY = y - player.getY() - 16;
            float distance = (float) Math.sqrt((x - player.getX()) * (x - player.getX()) + (y - player.getY()) * (y - player.getY()));

            velX = ((-1 / distance) * diffX);
            velY = ((-1 / distance) * diffY);
        }
        //if(x <= 0 || x >= Game.WIDTH - 16)  velX *= -1;
        //if(y <= 0 || y >= Game.HEIGHT - 32)  velY *= -1;

        UniversalHandler.getThingHandler().addEntityObject(new Trail(x,y,ID.Trail,Color.green,16,16,0.02f, GameObject.entities));

    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int)x,(int)y,16,16);
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,16,16);
    }
}
