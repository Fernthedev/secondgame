//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window extends Canvas {
    private static final long serialVersionUID = -3340118298870910984L;

    public Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(3);
        frame.setResizable(false);
        frame.setLocationRelativeTo((Component)null);
        frame.add(game);
        frame.setVisible(true);
        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage image = loader.loadImage("/icon.png");
        frame.setIconImage(image);
        game.start();
    }
}
