//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ThingHandler;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class Handler {

    private final HUD hud;
    
    public void serverTick() {


        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();
           // if (tempObject.id == ID.Trail) {
               // System.out.println(tempObject.getObjectID());
                tempObject.tick();
         //   }
        }
    }

    public void tick() {

    //    if(GAME.gameState != GAME.STATE.HOSTING) objects = ClientEntityHandler.gameObjects;
 //       else objects = EntityHandler.gameObjects;



        List<GameObject> gameObjects = new ArrayList<>(UniversalHandler.getThingHandler().getGameObjects());
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject tempObject = iterator.next();

            tempObject.tick();

            if(tempObject instanceof EntityPlayer)
                UniversalHandler.getThingHandler().collisionCheck((EntityPlayer) tempObject);
        }



    }

    public void render(Graphics g) {

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


        ThingHandler.gameObjects.clear();
        ThingHandler.gameObjectMap.clear();

    }

    public void clearEnemies() {
       // List<GameObject> gameObjects = new ArrayList<>(ClientEntityHandler.gameObjects);

        System.out.println("Clearing ");


        ThingHandler.gameObjects.clear();
        ThingHandler.gameObjectMap.clear();




        if (Game.gameState != Game.STATE.END) {
            addObject(UniversalHandler.mainPlayer);
            System.out.println("Added the player due to clear");
        }
    }



    public void setPlayerInfo(EntityPlayer player) {

        if(player != null) {
            UniversalHandler.getThingHandler().updatePlayerObject(null,player);
        }
       // addObject(player);
    }

    public Handler(HUD hud) {
        this.hud = hud;
    }
}
