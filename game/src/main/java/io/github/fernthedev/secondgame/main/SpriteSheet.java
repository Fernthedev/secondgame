package io.github.fernthedev.secondgame.main;

import java.awt.image.BufferedImage;

class SpriteSheet {


    private final BufferedImage sprite;

    public SpriteSheet(BufferedImage ss) {
        this.sprite = ss;

    }

    public BufferedImage grapImage(int col,int row,int width, int height) {
        return sprite.getSubimage((row*32) - 32,(col*32) -32,width,height);
    }

}
