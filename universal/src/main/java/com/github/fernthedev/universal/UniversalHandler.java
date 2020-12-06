package com.github.fernthedev.universal;

import com.github.fernthedev.IGame;
import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniversalHandler {

    public static final int WIDTH = 640,HEIGHT =  WIDTH / 12*9;

    public static final int TICK_WAIT = 5;

    public static final int MULTIPLAYER_PORT = 3000;

    @Getter
    @Setter
    private static IGame iGame;

    @Getter
    @Setter
    private static boolean running = true;

    public static final Gson gson = new Gson();
    public static final Random RANDOM = new Random();


}
