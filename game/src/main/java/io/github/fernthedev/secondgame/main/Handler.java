//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.github.fernthedev.secondgame.main.netty.client.ClientEntityHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class Handler {

    private final HUD hud;
    
    public void serverTick() {


        List<GameObject> gameObjects = new ArrayList<>(ClientEntityHandler.gameObjects);
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();
            if (tempObject.id != ID.Trail) {
               // System.out.println(tempObject.getObjectID());
                tempObject.tick();
            }
        }
    }

    public void tick() {

    //    if(Game.gameState != Game.STATE.Hosting) objects = ClientEntityHandler.gameObjects;
 //       else objects = EntityHandler.gameObjects;



        List<GameObject> gameObjects = new ArrayList<>(ClientEntityHandler.gameObjects);
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();

            tempObject.tick();
        }
    }

    public void render(Graphics g) {

        //System.out.println("Rendering objects " + gameObjectList);

        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();
            if (tempObject != null) {
                try {
                    TimeUnit.SECONDS.sleep((long) 0.3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                tempObject.render(g);
            }
        }

       // System.out.println(gameObjects);

    }

    public void addObject(GameObject object) {
        //System.out.println(UniversalHandler.getThingHandler());



        UniversalHandler.getThingHandler().addEntityObject(object);
    }

    public void removeObject(GameObject object) {
        UniversalHandler.getThingHandler().removeEntityObject(object);
    }


    public void clearObjects() {
        // List<GameObject> gameObjects = new ArrayList<>(ClientEntityHandler.gameObjects);

        System.out.println("Clearing ");


        ClientEntityHandler.gameObjects.clear();
        ClientEntityHandler.gameObjectMap.clear();

    }

    public void clearEnemies() {
       // List<GameObject> gameObjects = new ArrayList<>(ClientEntityHandler.gameObjects);

        System.out.println("Clearing ");


        ClientEntityHandler.gameObjects.clear();
        ClientEntityHandler.gameObjectMap.clear();




        if (Game.gameState != Game.STATE.End) {
            addObject(Game.mainPlayer);
            System.out.println("Added the player due to clear");
        }
    }



    public void setPlayerInfo(UniversalPlayer player) {

        if(player != null) {
            UniversalHandler.getThingHandler().updatePlayerObject(Game.getServerClientObject(),player);
        }
       // addObject(player);
    }

    public Handler(HUD hud) {
        this.hud = hud;
    }
}
