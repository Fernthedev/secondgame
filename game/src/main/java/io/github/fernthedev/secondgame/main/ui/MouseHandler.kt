package io.github.fernthedev.secondgame.main.ui;

import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.ui.api.Screen;
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MouseHandler extends MouseAdapter {

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (Game.getScreen() != null) {
            Screen screen = Game.getScreen();

            new ArrayList<>(screen.getButtonList()).parallelStream().forEach(screenButton -> {
                if (mouseOver(mx, my, screenButton)) {
                    screenButton.getOnClick().run();
                }
            });
        } else {
            // Debug tool
            if (Game.getMainPlayer() != null && StaticHandler.isDebug()) {
                EntityPlayer entityPlayer = Game.getMainPlayer();

                entityPlayer.setX(mx);
                entityPlayer.setY(my);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return (mx > x && mx < x + width) && (my > y && my < y + height);
    }

    private boolean mouseOver(int mx, int my, ScreenButton ui) {
        return (mx > ui.getRenderedLocation().getX() && mx < ui.getRenderedLocation().getX() + ui.getButtonSize().getWidth()) && (my > ui.getRenderedLocation().getY() && my < ui.getRenderedLocation().getY() + ui.getButtonSize().getHeight());
    }
}
