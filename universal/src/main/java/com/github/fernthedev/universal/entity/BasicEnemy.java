package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
public class BasicEnemy extends GameObject {

    public BasicEnemy(int x, int y, EntityID entityId) {
        super(x, y, 16, 16, entityId, Color.RED);


        velX = 5;
        velY = 5;
    }

    protected BasicEnemy() {
        super();
    }

    public void tick() {
        setX((float) (x + velX));
        setY((float) (y + velY));

        if (x <= 0 || x >= UniversalHandler.WIDTH - (float) width) velX *= -1;
        if (y <= 0 || y >= UniversalHandler.HEIGHT - (float) height*2) velY *= -1;



        //System.out.println("i was ticked");
    }


}