//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    private Game game;

    private boolean[] keyDown = new boolean[4];
    private boolean[] joystick = new boolean[4];
    private boolean ifJoystick;

    public KeyInput(Handler handler,Game game) {
        this.handler = handler;
        this.game = game;

        keyDown[0] = false;
        keyDown[1] = false;
        keyDown[2] = false;
        keyDown[3] = false;
        GLFW.glfwInit();
        GLFW.glfwPollEvents();

        if(glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            System.out.println("connected controller " + glfwGetJoystickName(GLFW_JOYSTICK_1));
        }
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println("Some key pressed ");
        int key = e.getKeyCode();

        for(int i =0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.Player) {
                //KEY EVENTS FOR PLAYER 1
                if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) { tempObject.setVelY(-5); keyDown[0] = true; ifJoystick = false;}
                if(key == KeyEvent.VK_S|| key == KeyEvent.VK_DOWN) {tempObject.setVelY(5); keyDown[1] = true; ifJoystick = false;}
                if(key == KeyEvent.VK_D|| key == KeyEvent.VK_RIGHT) {tempObject.setVelX(5); keyDown[2] = true; ifJoystick = false;}
                if(key == KeyEvent.VK_A|| key == KeyEvent.VK_LEFT) { tempObject.setVelX(-5); keyDown[3] = true; ifJoystick = false;}

            }

        }
        if(key == KeyEvent.VK_P){
            if(Game.gameState == Game.STATE.Game) {
                if (Game.paused)
                    Game.paused = false;
                else Game.paused = true;

            }
        }


        if(key == KeyEvent.VK_ESCAPE) System.exit(0);
        //System.out.println(key);
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println(key);
        for(int i =0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.Player) {
                //KEY EVENTS FOR PLAYER 1
                if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP ) keyDown[0] = false;
                if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) keyDown[1] = false;
                if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) keyDown[2] = false;
                if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) keyDown[3] = false;


            }


        }
    }

    public void tick() {
        ByteBuffer buttons = GLFW.glfwGetJoystickButtons(GLFW_JOYSTICK_1);

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.id == ID.Player) {

                if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
                    assert buttons != null;
                    //UP
                    if(buttons.get(10) != (byte) 0) {
                        joystick[0] = true;
                        keyDown[0] = true;
                        tempObject.setVelY(-5);
                    }else{
                        if(ifJoystick){
                            keyDown[0] = false;
                        }
                    }

                    //RIGHT
                    if(buttons.get(11) != (byte) 0) {
                        keyDown[2] = true;
                        joystick[2] = true;
                        tempObject.setVelX(5);
                    }else{
                        if(ifJoystick){
                            keyDown[2] = false;
                        }
                    }

                    //DOWN
                    if(buttons.get(12) != (byte) 0) {
                        keyDown[1] = true;
                        joystick[1] = true;
                        tempObject.setVelY(5);
                    }else{
                        if(ifJoystick){
                            keyDown[1] = false;
                        }
                    }

                    //LEFT
                    if(buttons.get(13) != (byte) 0) {
                        keyDown[3] = true;
                        ifJoystick = true;
                        tempObject.setVelX(-5);
                    }else{
                        if(ifJoystick){
                            keyDown[3] = false;
                        }
                    }

                }
                //VERTICAL
                if (!keyDown[0] && !keyDown[1]) tempObject.setVelY(0);

                //HORIZONTAL
                if (!keyDown[2] && !keyDown[3]) tempObject.setVelX(0);
            }
        }

    }
}