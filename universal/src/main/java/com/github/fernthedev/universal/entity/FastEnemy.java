package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
public class FastEnemy extends GameObject {

    public FastEnemy(int x, int y, EntityID entityId) {
        super(x, y, 16, 16, entityId, Color.CYAN);



        velX = 5;
        velY = 7;
    }

    protected FastEnemy() {
        super();
    }

    public void tick() {
        x += velX;
        y += velY;

        if(x <= 0 || x >= UniversalHandler.WIDTH - width)  velX *= -1;
        if(y <= 0 || y >= UniversalHandler.HEIGHT - height*2)  velY *= -1;

    }
}
