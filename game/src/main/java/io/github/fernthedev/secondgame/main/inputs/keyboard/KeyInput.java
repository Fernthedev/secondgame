//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main.inputs.keyboard;

import com.github.fernthedev.packets.PlayerUpdates.SendToGetInfo;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.HUD;
import io.github.fernthedev.secondgame.main.Handler;
import io.github.fernthedev.secondgame.main.inputs.InputHandler;
import io.github.fernthedev.secondgame.main.inputs.InputType;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private final boolean[] keyDown = new boolean[4];


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
        if (Game.mainPlayer != null) {
            boolean toUpdate = false;

            // System.out.println(GamemainPlayer.getObjectID() + " " + Game.mainPlayer.getObjectID());
            //KEY EVENTS FOR PLAYER 1
            if ((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && Game.mainPlayer.getVelY() != -5) {
                Game.mainPlayer.setVelY(-5);
                toUpdate = true;
                keyDown[0] = true;
            }
            if ((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && Game.mainPlayer.getVelY() != 5) {
                Game.mainPlayer.setVelY(5);
                toUpdate = true;
                keyDown[1] = true;
            }
            if ((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && Game.mainPlayer.getVelX() != 5) {
                Game.mainPlayer.setVelX(5);
                toUpdate = true;
                keyDown[2] = true;
            }
            if ((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && Game.mainPlayer.getVelX() != -5) {
                Game.mainPlayer.setVelX(-5);
                toUpdate = true;
                keyDown[3] = true;
            }

            if(toUpdate) update();

            //  System.out.println(KeyEvent.getKeyText(key));


            // Game.mainPlayer = GamemainPlayer;
            // System.out.println(Game.mainPlayer);
            //  if(Game.gameState == Game.STATE.Hosting )
            //  if(Game.STATE.Game == Game.gameState || Game.gameState == Game.STATE.Hosting) {

            //      }
        }
        if (key == KeyEvent.VK_P) {
            if (Game.gameState == Game.STATE.Game) {
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


        if (Game.mainPlayer != null) {
            boolean toUpdate = false;
            


            //VERTICAL
            if (!keyDown[0] && !keyDown[1] && Game.mainPlayer.getVelY() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
                Game.mainPlayer.setVelY(0);
                toUpdate = true;
            }

            //HORIZONTAL
            if (!keyDown[2] && !keyDown[3] && Game.mainPlayer.getVelX() != 0 && InputHandler.inputType == InputType.KEYBOARD) {
                Game.mainPlayer.setVelX(0);
                toUpdate = true;
            }
            
            if(toUpdate) update();

            // System.out.println(Game.mainPlayer.getObjectID() + "\n" + tempObject.getObjectID());


            //  System.out.println(Game.mainPlayer.getVelX() + "\n" + Game.mainPlayer.getVelY() + "\n");
/*
            if (Game.gameState == Game.STATE.InServer || Game.gameState == Game.STATE.Hosting) {

                SendPlayerInfoPacket sendPlayerInfoPacket = new SendPlayerInfoPacket(Game.mainPlayer);

                if (Game.gameState == Game.STATE.InServer)
                    Client.getClientThread().sendObject(sendPlayerInfoPacket);
                else if (Game.gameState == Game.STATE.Hosting) {
                    //     System.out.println("Thing! 2");
                    //UniversalHandler.getThingHandler().updatePlayerObject(Game.mainPlayer);
                    //   System.out.println("Thing! 3");
                    Server.sendObjectToAllPlayers(sendPlayerInfoPacket);
                    //    System.out.println("Thing! 4");
                }
            }*/

        }
    }



    private void update() {
        if(Game.gameState == Game.STATE.Game || Game.gameState == Game.STATE.InServer || Game.gameState == Game.STATE.Hosting) {
            UniversalHandler.getThingHandler().updatePlayerObject(Game.getServerClientObject(),Game.mainPlayer);
            //System.out.println("Updated " + Game.mainPlayer);
        }

        if(Game.gameState == Game.STATE.InServer) {
            Game.mainPlayer.setHealth(HUD.HEALTH);
            Game.sendPacket(new SendToGetInfo(new UniversalPlayer(Game.mainPlayer)));
        }
    }
}