package io.github.fernthedev.secondgame.main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window extends Canvas {


    private static final long serialVersionUID = -3340118298870910984L;

    public Window(int width,int height,String title,Game game) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width,height));
        frame.setMaximumSize(new Dimension(width,height));
        frame.setMinimumSize(new Dimension(width,height));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);

        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage image = loader.loadImage("/icon.png");
        frame.setIconImage(image);

        //frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        game.start();
    }
}
