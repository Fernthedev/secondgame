package io.github.fernthedev.secondgame.main.ui.screens;

import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.HUD;
import io.github.fernthedev.secondgame.main.ui.api.Screen;
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton;

import java.awt.*;

public class EndScreen extends Screen {

    private static final String TITLE = "GAME Over";

    public EndScreen() {
        super(TITLE);
        addMenuParticles();
        setParentScreenOnSet = false;
        parentScreen = new MainMenu();
    }

    @Override
    protected void draw(Graphics g) {
        HUD hud = Game.getHud();
        g.setColor(Color.WHITE);
        g.drawString("Score: " + hud.getScore(), 150, incrementY(-buttonSpacing + 30));
        g.drawString("Level: " + hud.getLevel(), 150, incrementY(-buttonSpacing + 30));
        g.drawString("Coin: " + Game.getHud().getCoin(), 150, incrementY(-buttonSpacing + 30));

        addButton(new ScreenButton("Try Again", () -> {
            hud.setLevel(1);
            hud.setScore(0);
            Game.setScreen(new MainMenu());
        }));
        // g.drawRect(210, 350, 200, 64);
        //    g.drawString("Try Again", 245, 390);
    }
}
