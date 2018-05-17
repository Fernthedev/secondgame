//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import io.github.fernthedev.secondgame.main.Game.STATE;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    private Game game;

    private boolean[] keyDown = new boolean[4];

    public KeyInput(Handler handler,Game game) {
        this.handler = handler;
        this.game = game;
        keyDown[0] = false;
        keyDown[1] = false;
        keyDown[2] = false;
        keyDown[3] = false;
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println("Some key pressed ");
        int key = e.getKeyCode();

        for(int i =0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if(tempObject.getId() == ID.Player) {
                //KEY EVENTS FOR PLAYER 1
                if(key == KeyEvent.VK_W) { tempObject.setVelY(-5); keyDown[0] = true; }
                if(key == KeyEvent.VK_S) {tempObject.setVelY(5); keyDown[1] = true;}
                if(key == KeyEvent.VK_D) {tempObject.setVelX(5); keyDown[2] = true;}
                if(key == KeyEvent.VK_A) { tempObject.setVelX(-5); keyDown[3] = true;}

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
                if(key == KeyEvent.VK_W) keyDown[0] = false;
                if(key == KeyEvent.VK_S) keyDown[1] = false;
                if(key == KeyEvent.VK_D) keyDown[2] = false;
                if(key == KeyEvent.VK_A) keyDown[3] = false;

                //VERTICAL
                if(!keyDown[0] && !keyDown[1]) tempObject.setVelY(0);

                //HORIZONTAL
                if(!keyDown[2] && !keyDown[3]) tempObject.setVelX(0);
            }


        }
    }

}