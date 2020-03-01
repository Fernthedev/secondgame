package io.github.fernthedev.secondgame.main.UI;

import io.github.fernthedev.secondgame.main.Game;

import java.awt.*;

import static io.github.fernthedev.secondgame.main.UI.Menu.mouseOverUIList;

public abstract class MouseOverUI {
    private final int x;
    private final int width;
    private final int y;
    private final int height;

    private String boxID;

    private String string = "";
    private int stringX,stringY;

    private final Game.STATE state;

    private final Graphics g;

    public int getX() {
        return x;
    }

    public int getWidth() {
        return width;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    MouseOverUI(Graphics g, int x, int y, int width, int height, Game.STATE state) {
        this.x = x;
        this.width = width;
        this.y = y;
        this.height = height;
        this.state = state;
        this.g = g;

        g.drawRect(x, y, width, height);
        mouseOverUIList.add(this);
    }

    void drawString(String string, int stringX, int stringY) {
        g.drawString(string,stringX,stringY);
        this.string = string;
        this.stringX = stringX;
        this.stringY = stringY;
    }

    void drawString(String string) {
        g.drawString(string,x+60,y+40);
        this.string = string;
        this.stringX = x+60;
        this.stringY = y+40;
    }

    public String getBoxID() {
        return boxID;
    }

    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }

    public abstract void onClick();


    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getStringX() {
        return stringX;
    }

    public void setStringX(int stringX) {
        this.stringX = stringX;
    }

    public int getStringY() {
        return stringY;
    }

    public void setStringY(int stringY) {
        this.stringY = stringY;
    }

    public Game.STATE getState() {
        return state;
    }

    @Override
    public String toString() {
        return string + " " + stringX + " " + stringY + " box " + x + " " + y + " " + width + ":" + height;
    }
}


