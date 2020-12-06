package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.ToString;

import java.awt.*;
import java.util.Random;

@ToString(callSuper = true)
public class MenuParticle extends GameObject {


    private static Random r = UniversalHandler.RANDOM;

    private int dir = 0;


    public MenuParticle(int x, int y, EntityID entityId) {
        super(x, y, 16, 16, entityId, new Color(r.nextInt(255), r.nextInt(255),r.nextInt(255)));



        dir = r.nextInt(2);

        velX = (r.nextInt(7 - -7) + -7);
        velY = (r.nextInt(7 - -7) + -7);

        if(velX == 0) velX = 1;
        if(velY == 0) velY = 1;
    }

    public MenuParticle(GameObject gameObject) {
        super(gameObject);
        if(gameObject instanceof MenuParticle) {
            MenuParticle menuParticle = (MenuParticle) gameObject;
            this.dir = menuParticle.dir;
        }
    }



    public void tick() {

        if (velX == 0) velX = (r.nextInt(7 - -7) + -7);
        if (velY == 0) velY = (r.nextInt(7 - -7) + -7);

        x += velX;
        y += velY;

        if(x <= 0 || x >= UniversalHandler.WIDTH - 32)  velX *= -1;
        if(y <= 0 || y >= UniversalHandler.HEIGHT - 32)  velY *= -1;
    }

}
