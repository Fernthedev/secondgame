package io.github.fernthedev.secondgame.main.ui.screens;

import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.ui.api.Screen;
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton;

import java.awt.*;

public class MainMenu extends Screen {

    private static final String TITLE = "MENU";

    public MainMenu() {
        super(TITLE);
        addMenuParticles();
    }

    @Override
    public void draw(Graphics g) {

        addButton(new ScreenButton("Multiplayer", () -> Game.setScreen(new MultiplayerScreen())));
        addButton(new ScreenButton("Play", () -> Game.getStaticEntityRegistry().startGame()));
        addButton(new ScreenButton("Help", () -> Game.setScreen(new HelpMenu())));
        addButton(DEFAULT_BACK_BUTTON);


//        button = new MouseOverUI(g, 210, getButtonY(), BUTTON_WIDTH + 60, BUTTON_HEIGHT, Game.STATE.MENU) {
//            @Override
//            public void onClick() {
//                Game.gameState = Game.STATE.MULTIPLAYER;
//            }
//        };
//        button.drawString("MULTIPLAYER", BUTTON_STRING_SIZE);
//        mouseOverUIList.add(button);

//        button = new MouseOverUI(g, 210, getButtonY(), BUTTON_WIDTH, BUTTON_HEIGHT, Game.STATE.MENU) {
//            @Override
//            public void onClick() {
//                Game.gameState = Game.STATE.GAME;
//                startGame();
//            }
//        };
//
//        button.drawString("Play", BUTTON_STRING_SIZE);
//        mouseOverUIList.add(button);


        // g.drawRect(210, 150, 200, 64);
        //  g.drawString("Play", 270, 190);

        // g.drawRect(210, 150, 200, 64);
        //    g.drawString("Helpi", 270, 190);




//        button = new MouseOverUI(g, 210, getButtonY(), BUTTON_WIDTH, BUTTON_HEIGHT, Game.STATE.MENU) {
//            @Override
//            public void onClick() {
//                Game.gameState = Game.STATE.HELP;
//            }
//        };
//
//        button.drawString("HELP", BUTTON_STRING_SIZE);
//        mouseOverUIList.add(button);

        // g.drawRect(210, 250, 200, 64);
        //     g.drawString("HELP", 270, 290);

//        button = new MouseOverUI(g, 210, getButtonY(), BUTTON_WIDTH, BUTTON_HEIGHT, Game.STATE.MENU) {
//            @Override
//            public void onClick() {
//                System.exit(0);
//            }
//        };
//        button.drawString("Quit", BUTTON_STRING_SIZE);
//        mouseOverUIList.add(button);
        //    g.drawRect(210, 350, 200, 64);
        //    g.drawString("Quit", 270, 390);
    }

    @Override
    public void returnToParentScreen() {
        System.exit(0);
    }
}
