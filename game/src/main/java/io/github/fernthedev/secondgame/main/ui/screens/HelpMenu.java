package io.github.fernthedev.secondgame.main.ui.screens;

import io.github.fernthedev.secondgame.main.ui.api.Screen;

import java.awt.*;

public class HelpMenu extends Screen {

    private static final String TITLE = "HELP";

    public HelpMenu() {
        super(TITLE);
        addMenuParticles();
    }


    @Override
    protected void draw(Graphics g) {
        g.setFont(textFont.getFont());
        g.setColor(textFont.getColor());
        g.drawString("Use WASD or arrow keys please", buttonDefaultX-90, buttonDefaultYLocStart + 130);


        addButton(DEFAULT_BACK_BUTTON);
    }
}
