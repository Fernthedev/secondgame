//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BufferedImageLoader {
    BufferedImage image;

    public BufferedImageLoader() {
    }

    public BufferedImage loadImage(String path) {
        try {
            this.image = ImageIO.read(this.getClass().getResource(path));
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return this.image;
    }
}
