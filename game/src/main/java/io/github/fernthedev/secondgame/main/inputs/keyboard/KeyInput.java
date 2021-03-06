//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main.inputs.keyboard;

import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import com.github.fernthedev.universal.UniversalHandler;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.inputs.InputHandler;
import io.github.fernthedev.secondgame.main.inputs.InputType;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyInput extends KeyAdapter {

    private final Map<Integer, KeyButton> keyButtonMap = new HashMap<>();

    private boolean toUpdate = false;
    private final Game game;



    public KeyInput(Game game) {
        this.game = game;

//        glfwPollEvents();

        /*GLFW.glfwInit();
        GLFW.glfwPollEvents();

        if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            Game.getLogger().info("connected controller " + glfwGetJoystickName(GLFW_JOYSTICK_1));
        }*/
    }

    @Override
    public void keyPressed(KeyEvent e) {
        InputHandler.inputType = InputType.KEYBOARD;
        //Game.getLogger().info("Some key pressed ");
        int key = e.getKeyCode();

        registerKey(key, true);

        if (key == KeyEvent.VK_P) {
            if (Game.getScreen() == null) {
                Game.paused = !Game.paused;

            }
        }


        if (key == KeyEvent.VK_ESCAPE) game.stop();
        //Game.getLogger().info(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //Game.getLogger().info(key);


        registerKey(key, false);


        //KEY EVENTS FOR PLAYER 1
//        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) keyDown[0] = false;
//        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) keyDown[1] = false;
//        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) keyDown[2] = false;
//        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) keyDown[3] = false;

    }


    private KeyButton registerKey(int key) {
        KeyButton button = keyButtonMap.get(key);
        if (button == null) {
            button = new KeyButton(key, false);
            keyButtonMap.put(key, button);
        };

        return button;
    }

    private KeyButton registerKey(int key, boolean held) {
        KeyButton button = keyButtonMap.get(key);
        if (button == null) {
            button = new KeyButton(key, held);
            keyButtonMap.put(key, button);
        }else button.setHeld(held);

        return button;
    }

    public void tick() {

        // Game.getLogger().info("Thing! " + gameObjectList.size());


        if (Game.getMainPlayer() != null && InputHandler.inputType == InputType.KEYBOARD) {

            int velX = 0, velY = 0;

            int velMultiplier = UniversalHandler.PLAYER_VEL_MULTIPLIER;
            if (registerKey(KeyEvent.VK_W).isHeld()) {
                velY += -velMultiplier;
            }

            if (registerKey(KeyEvent.VK_S).isHeld()) {
                velY += velMultiplier;
            }
            if (registerKey(KeyEvent.VK_D).isHeld()) {
                velX += velMultiplier;
            }
            if (registerKey(KeyEvent.VK_A).isHeld()) {
                velX += -velMultiplier;
            }

            if (Game.getMainPlayer().getVelX() != velX || Game.getMainPlayer().getVelY() != velY) toUpdate = true;

            //VERTICAL
//            if (!keyDown[0] && !keyDown[1] && Game.mainPlayer.getVelY() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
//                Game.mainPlayer.setVelY(0);
//                toUpdate = true;
//            }
//
//            //HORIZONTAL
//            if (!keyDown[2] && !keyDown[3] && Game.mainPlayer.getVelX() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
//                Game.mainPlayer.setVelX(0);
//                toUpdate = true;
//            }

            if(toUpdate) {
                Game.getMainPlayer().setVelX(velX);
                Game.getMainPlayer().setVelY(velY);
                Game.getStaticEntityRegistry().setPlayerInfo(Game.getMainPlayer());
                update();
            }

            // Game.getLogger().info(GAME.mainPlayer.getObjectID() + "\n" + tempObject.getObjectID());


            //  Game.getLogger().info(GAME.mainPlayer.getVelX() + "\n" + GAME.mainPlayer.getVelY() + "\n");
/*
            if (GAME.gameState == GAME.STATE.IN_SERVER || GAME.gameState == GAME.STATE.HOSTING) {

                SendToServerPlayerInfoPacket sendPlayerInfoPacket = new SendToServerPlayerInfoPacket(GAME.mainPlayer);

                if (GAME.gameState == GAME.STATE.IN_SERVER)
                    Client.getClientThread().sendObject(sendPlayerInfoPacket);
                else if (GAME.gameState == GAME.STATE.HOSTING) {
                    //     Game.getLogger().info("Thing! 2");
                    //UniversalHandler.getThingHandler().updatePlayerObject(GAME.mainPlayer);
                    //   Game.getLogger().info("Thing! 3");
                    Server.sendObjectToAllPlayers(sendPlayerInfoPacket);
                    //    Game.getLogger().info("Thing! 4");
                }
            }*/

        }
    }



    private void update() {
        toUpdate = false;
        /*if(Game.gameState == Game.STATE.GAME || Game.gameState == Game.STATE.IN_SERVER || Game.gameState == Game.STATE.HOSTING) {
            UniversalHandler.getThingHandler().updatePlayerObject(null,Game.mainPlayer);
            //Game.getLogger().info("Updated " + GAME.mainPlayer);
        }*/

        if(Game.getScreen() == null && Game.getClient() != null && Game.getClient().isRegistered() && Game.getMainPlayer() != null) {
           // Game.mainPlayer.setHealth(Game.mainPlayer.getHealth());
            Game.getClient().sendObject(new SendToServerPlayerInfoPacket(Game.getMainPlayer(), Game.getStaticEntityRegistry().getObjectsAndHashCode()));
            Game.getLogger().debug("Updated player");
        }
    }
}