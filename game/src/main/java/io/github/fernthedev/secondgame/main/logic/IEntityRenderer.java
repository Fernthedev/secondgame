package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.universal.GameObject;

import java.awt.*;

public interface IEntityRenderer<T extends GameObject> {

    void render(Graphics g, T gameObject);
}
