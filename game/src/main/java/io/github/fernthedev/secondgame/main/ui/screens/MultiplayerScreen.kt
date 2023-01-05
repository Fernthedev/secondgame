package io.github.fernthedev.secondgame.main.ui.screens

import io.github.fernthedev.secondgame.main.Game
import io.github.fernthedev.secondgame.main.ui.api.Screen
import io.github.fernthedev.secondgame.main.ui.api.ScreenButton
import java.awt.Graphics2D

class MultiplayerScreen : Screen(TITLE) {
    init {
        Screen.Companion.addMenuParticles()
    }

    override fun draw(g: Graphics2D) {
        addButton(ScreenButton("Join") { Game.setupJoiningMultiplayer() })

        //                    server.setPlayerStarter(ServerGameObject.getObjectType(new ServerGameObject(Game.mainPlayer)));
        //                    Thread thread = new Thread(server);
        //                    thread.start();
        //UniversalHandler.getThingHandler().addEntityObject();
        // server.startServer();
        addButton(ScreenButton("Host") { Game.setupHostingMultiplayer() })
        addButton(DEFAULT_BACK_BUTTON)
    }

    companion object {
        private const val TITLE = "MULTIPLAYER"
    }
}