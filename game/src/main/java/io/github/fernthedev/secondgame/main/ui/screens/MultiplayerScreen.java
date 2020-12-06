package io.github.fernthedev.secondgame.main.ui.screens;

import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.ui.api.Screen;
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton;

import java.awt.*;

public class MultiplayerScreen extends Screen {

    private static final String TITLE = "MULTIPLAYER";

    public MultiplayerScreen() {
        super(TITLE);
        addMenuParticles();
    }

    @Override
    protected void draw(Graphics g) {

        addButton(new ScreenButton("Join", Game::setupJoiningMultiplayer));

        //                    server.setPlayerStarter(ServerGameObject.getObjectType(new ServerGameObject(Game.mainPlayer)));
        //                    Thread thread = new Thread(server);
        //                    thread.start();
        //UniversalHandler.getThingHandler().addEntityObject();
        // server.startServer();
        addButton(new ScreenButton("Host", Game::setupHostingMultiplayer));

        addButton(DEFAULT_BACK_BUTTON);
    }
}
