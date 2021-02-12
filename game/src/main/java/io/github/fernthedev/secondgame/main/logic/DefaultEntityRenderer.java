package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.universal.GameObject;

import java.awt.*;

public class DefaultEntityRenderer implements IEntityRenderer<GameObject> {
    @Override
    public void render(Graphics g, GameObject gameObject, float drawX, float drawY) {
        g.setColor(gameObject.getColor());
        g.fillRect((int)drawX, (int)drawY, gameObject.getWidth(), gameObject.getHeight());
    }
}
