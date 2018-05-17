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

    public KeyInput(Handler handler, Game game) {
        this.handler = handler;
        this.game = game;
        this.keyDown[0] = false;
        this.keyDown[1] = false;
        this.keyDown[2] = false;
        this.keyDown[3] = false;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i = 0; i < this.handler.object.size(); ++i) {
            GameObject tempObject = (GameObject)this.handler.object.get(i);
            if (tempObject.getId() == ID.Player) {
                if (key == 87) {
                    tempObject.setVelY(-5);
                    this.keyDown[0] = true;
                }

                if (key == 83) {
                    tempObject.setVelY(5);
                    this.keyDown[1] = true;
                }

                if (key == 68) {
                    tempObject.setVelX(5);
                    this.keyDown[2] = true;
                }

                if (key == 65) {
                    tempObject.setVelX(-5);
                    this.keyDown[3] = true;
                }
            }
        }

        if (key == 80 && Game.gameState == STATE.Game) {
            if (Game.paused) {
                Game.paused = false;
            } else {
                Game.paused = true;
            }
        }

        if (key == 27) {
            System.exit(0);
        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i = 0; i < this.handler.object.size(); ++i) {
            GameObject tempObject = (GameObject)this.handler.object.get(i);
            if (tempObject.getId() == ID.Player) {
                if (key == 87) {
                    this.keyDown[0] = false;
                }

                if (key == 83) {
                    this.keyDown[1] = false;
                }

                if (key == 68) {
                    this.keyDown[2] = false;
                }

                if (key == 65) {
                    this.keyDown[3] = false;
                }

                if (!this.keyDown[0] && !this.keyDown[1]) {
                    tempObject.setVelY(0);
                }

                if (!this.keyDown[2] && !this.keyDown[3]) {
                    tempObject.setVelX(0);
                }
            }
        }

    }
}
