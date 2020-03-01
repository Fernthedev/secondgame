package io.github.fernthedev.secondgame.main.UI;

import io.github.fernthedev.secondgame.main.Game;

import java.awt.*;

public class TextOverBox extends MouseOverUI {

    private String text;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    TextOverBox(Graphics g, int x, int y, int width, int height, Game.STATE state) {
        super(g, x, y, width, height, state);
    }



    @Override
    public void onClick() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
