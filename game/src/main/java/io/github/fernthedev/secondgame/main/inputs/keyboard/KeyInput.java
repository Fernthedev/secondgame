//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main.inputs.keyboard;

import com.github.fernthedev.packets.PlayerUpdates.SendToGetInfo;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.Handler;
import io.github.fernthedev.secondgame.main.inputs.InputHandler;
import io.github.fernthedev.secondgame.main.inputs.InputType;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private final boolean[] keyDown = new boolean[4];


    private boolean toUpdate = false;
    private Game game;

    public KeyInput(Handler handler, Game game) {
        this.game = game;
        keyDown[0] = false;
        keyDown[1] = false;
        keyDown[2] = false;
        keyDown[3] = false;
        /*GLFW.glfwInit();
        GLFW.glfwPollEvents();

        if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            System.out.println("connected controller " + glfwGetJoystickName(GLFW_JOYSTICK_1));
        }*/
    }

    public void keyPressed(KeyEvent e) {
        InputHandler.inputType = InputType.KEYBOARD;
        //System.out.println("Some key pressed ");
        int key = e.getKeyCode();
        if (UniversalHandler.mainPlayer != null) {

            // System.out.println(GamemainPlayer.getObjectID() + " " + GAME.mainPlayer.getObjectID());
            //KEY EVENTS FOR PLAYER 1
            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && UniversalHandler.mainPlayer.getVelY() != -5) {
                UniversalHandler.mainPlayer.setVelY(-5);
                toUpdate = true;
                keyDown[0] = true;
            }
            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && UniversalHandler.mainPlayer.getVelY() != 5) {
                UniversalHandler.mainPlayer.setVelY(5);
                toUpdate = true;
                keyDown[1] = true;
            }
            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && UniversalHandler.mainPlayer.getVelX() != 5) {
                UniversalHandler.mainPlayer.setVelX(5);
                toUpdate = true;
                keyDown[2] = true;
            }
            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && UniversalHandler.mainPlayer.getVelX() != -5) {
                UniversalHandler.mainPlayer.setVelX(-5);
                toUpdate = true;
                keyDown[3] = true;
            }

            if(toUpdate) update();

            //  System.out.println(KeyEvent.getKeyText(key));


            // GAME.mainPlayer = GamemainPlayer;
            // System.out.println(GAME.mainPlayer);
            //  if(GAME.gameState == GAME.STATE.HOSTING )
            //  if(GAME.STATE.GAME == GAME.gameState || GAME.gameState == GAME.STATE.HOSTING) {

            //      }
        }
        if (key == KeyEvent.VK_P) {
            if (Game.gameState == Game.STATE.GAME) {
                Game.paused = !Game.paused;

            }
        }


        if (key == KeyEvent.VK_ESCAPE) game.stop();
        //System.out.println(key);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println(key);

        //KEY EVENTS FOR PLAYER 1
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) keyDown[0] = false;
        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) keyDown[1] = false;
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) keyDown[2] = false;
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) keyDown[3] = false;

    }

    public void tick() {

        // System.out.println("Thing! " + gameObjectList.size());


        if (UniversalHandler.mainPlayer != null) {
            


            //VERTICAL
            if (!keyDown[0] && !keyDown[1] && UniversalHandler.mainPlayer.getVelY() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
                UniversalHandler.mainPlayer.setVelY(0);
                toUpdate = true;
            }

            //HORIZONTAL
            if (!keyDown[2] && !keyDown[3] && UniversalHandler.mainPlayer.getVelX() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
                UniversalHandler.mainPlayer.setVelX(0);
                toUpdate = true;
            }
            
            if(toUpdate) update();

            // System.out.println(GAME.mainPlayer.getObjectID() + "\n" + tempObject.getObjectID());


            //  System.out.println(GAME.mainPlayer.getVelX() + "\n" + GAME.mainPlayer.getVelY() + "\n");
/*
            if (GAME.gameState == GAME.STATE.IN_SERVER || GAME.gameState == GAME.STATE.HOSTING) {

                SendPlayerInfoPacket sendPlayerInfoPacket = new SendPlayerInfoPacket(GAME.mainPlayer);

                if (GAME.gameState == GAME.STATE.IN_SERVER)
                    Client.getClientThread().sendObject(sendPlayerInfoPacket);
                else if (GAME.gameState == GAME.STATE.HOSTING) {
                    //     System.out.println("Thing! 2");
                    //UniversalHandler.getThingHandler().updatePlayerObject(GAME.mainPlayer);
                    //   System.out.println("Thing! 3");
                    Server.sendObjectToAllPlayers(sendPlayerInfoPacket);
                    //    System.out.println("Thing! 4");
                }
            }*/

        }
    }



    private void update() {
        toUpdate = false;
        /*if(Game.gameState == Game.STATE.GAME || Game.gameState == Game.STATE.IN_SERVER || Game.gameState == Game.STATE.HOSTING) {
            UniversalHandler.getThingHandler().updatePlayerObject(null,UniversalHandler.mainPlayer);
            //System.out.println("Updated " + GAME.mainPlayer);
        }*/

        if(Game.gameState == Game.STATE.IN_SERVER) {
           // UniversalHandler.mainPlayer.setHealth(UniversalHandler.mainPlayer.getHealth());
            Game.sendPacket(new SendToGetInfo(new EntityPlayer(UniversalHandler.mainPlayer)));
            System.out.println("Updated player");
        }
    }
}