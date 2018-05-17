//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage sprite;

    public SpriteSheet(BufferedImage ss) {
        this.sprite = ss;
    }

    public BufferedImage grapImage(int col, int row, int width, int height) {
        BufferedImage img = this.sprite.getSubimage(row * 32 - 32, col * 32 - 32, width, height);
        return img;
    }
}
