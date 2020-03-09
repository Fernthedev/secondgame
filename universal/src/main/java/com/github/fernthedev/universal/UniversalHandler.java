package com.github.fernthedev.universal;

import com.github.fernthedev.IGame;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UniversalHandler {

    private static ThingHandler thingHandler;

    private static UniversalHandler instance = null;

    public static final int WIDTH = 640,HEIGHT =  WIDTH / 12*9;

    public static final int TICK_WAIT = 4;

    public static final int MULTIPLAYER_PORT = 3000;

    @Getter
    @Setter
    private static IGame iGame;



    @Getter
    @Setter
    private static boolean running = true;

    public static final Gson gson = new Gson();
    public static final Random RANDOM = new Random();

    @Deprecated
    public static final List<Thread> threads = Collections.synchronizedList(new ArrayList<>());

    public static synchronized UniversalHandler getInstance() {

        return instance == null ? instance = new UniversalHandler() : instance;
    }

    public void setup(ThingHandler methodInterface) {
        thingHandler = methodInterface;
    }


    static synchronized ThingHandler getThingHandler() {
        return thingHandler == null ? thingHandler = new ThingHandler() : thingHandler;
    }

}
