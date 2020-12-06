package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.universal.GameObject;

import java.awt.*;

public class DefaultEntityRenderer implements IEntityRenderer<GameObject> {
    @Override
    public void render(Graphics g, GameObject gameObject) {
        g.setColor(gameObject.getColor());
        g.fillRect((int)gameObject.getX(), (int)gameObject.getY(), gameObject.getWidth(), gameObject.getHeight());
    }
}
