//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.HUD;
import io.github.fernthedev.secondgame.main.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends UniversalPlayer {

    //Random r = new Random();
    private final Handler handler;
    private final HUD hud;

    public Player(GameObject gameObject,Handler handler, HUD hud) {
        super(gameObject);
        this.handler = handler;
        this.hud = hud;
    }

    public Player(UniversalPlayer universalPlayer,Handler handler,HUD hud) {
        super(universalPlayer);
        this.handler = handler;
        this.hud = hud;
    }

    /**
     * This is used for keeping velocity while using different coordinates.
     * @param keepPlayer The player with the velocity to keep
     * @param newPlayer The player with the coordinates to keep
     * @param handler
     * @param hud
     */
    public Player(UniversalPlayer keepPlayer,UniversalPlayer newPlayer,Handler handler,HUD hud) {
        this.handler = handler;
        this.hud = hud;

        this.velX = keepPlayer.getVelX();
        this.velY = keepPlayer.getVelY();
        this.id = ID.Player;

        this.x = newPlayer.getX();
        this.y = newPlayer.getY();
    }

    public Player(int x, int y, ID id, Handler handler, HUD hud,int objectID) {
        super(x, y, id,objectID);
        this.handler = handler;
        this.hud = hud;
    }


    public void tick() {
        if(Game.gameState != Game.STATE.InServer) {
            super.tick();
        }
        if(Game.gameState == Game.STATE.Game) {
            collision();
        }

        /*
        x += velX;
        y += velY;
        x = Game.clamp(x,0,Game.WIDTH - 37f);
        y = Game.clamp(y,0,Game.HEIGHT - 60f);
        if(Game.gameState == Game.STATE.Game) {
            handler.addObject(new Trail(x, y, ID.Trail, Color.WHITE, 32, 32, 0.05f, GameObject.entities));
            collision();

        }*/

    }

    private void collision() {
        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (GameObject tempObject : gameObjects) {
            if (tempObject.getId() == ID.BasicEnemey || tempObject.getId() == ID.FastEnemy || tempObject.getId() == ID.SmartEnemy) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    //COLLISION CODE
                    HUD.HEALTH -= 2;
                }
            }

            if (tempObject.getId() == ID.Coin) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    Game.getHud().plusCoin();
                    UniversalHandler.getThingHandler().removeEntityObject(tempObject);
                    System.out.println("COllision checking! COIN");
                    // this.handler.removeObject(tempObject);
                }
            }
        }
    }


    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect((int) x, (int) y, 32, 32);

    }
}
