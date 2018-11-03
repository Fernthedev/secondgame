package io.github.fernthedev.secondgame.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

class Window extends Canvas {

    private Game game;

    private static final long serialVersionUID = -3340118298870910984L;

    public Window(int width,int height,String title,Game game) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width,height));
        frame.setMaximumSize(new Dimension(width,height));
        frame.setMinimumSize(new Dimension(width,height));

        this.game = game;

        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);

        BufferedImageLoader loader = new BufferedImageLoader();
        BufferedImage image = loader.loadImage("/icon.png");
        frame.setIconImage(image);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
                // close sockets, etc
            }
        });

        //frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        game.start();
    }

    private void close() {
        System.out.println("Closing");
        game.stop();
        System.exit(0);
    }
}
