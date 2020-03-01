package com.github.fernthedev.universal;

import com.github.fernthedev.universal.entity.EntityPlayer;
import com.google.gson.Gson;

import java.util.Vector;

public class UniversalHandler {

    private static ThingHandler thingHandler;

    private static UniversalHandler instance = null;

    public static final int WIDTH = 640,HEIGHT =  WIDTH / 12*9;

    public static int tickWait = 5;

    public static boolean running = false;

    public static boolean isServer = false;

    public static EntityPlayer mainPlayer = null;

    public static Gson gson = new Gson();

    public static final Vector<Thread> threads = new Vector<>();

    public static synchronized UniversalHandler getInstance() {

        return instance == null ? instance = new UniversalHandler() : instance;
    }

    public void setup(ThingHandler methodInterface) {
        thingHandler = methodInterface;
    }


    public static synchronized ThingHandler getThingHandler() {
        return thingHandler == null ? thingHandler = new ThingHandler() : thingHandler;
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */

    public static float clamp(float var, float min, float max) {
        //System.out.println(min + " is min");
        //System.out.println(var + " is normal");
        if(var >= max) {
          //  System.out.println(max + " is max");
            return max;
        }
        else return Math.max(var, min);
    }
}
