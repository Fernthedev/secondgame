//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.IGame;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.config.gson.GsonConfig;
import com.github.fernthedev.game.server.GameServer;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import com.github.fernthedev.lightchat.client.Client;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler;
import com.github.fernthedev.lightchat.core.api.event.api.Listener;
import com.github.fernthedev.lightchat.core.codecs.general.compression.CompressionAlgorithm;
import com.github.fernthedev.lightchat.server.event.ServerStartupEvent;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import io.github.fernthedev.secondgame.main.inputs.joystick.JoystickHandler;
import io.github.fernthedev.secondgame.main.inputs.keyboard.KeyInput;
import io.github.fernthedev.secondgame.main.logic.NewClientEntityRegistry;
import io.github.fernthedev.secondgame.main.logic.Settings;
import io.github.fernthedev.secondgame.main.netty.client.PacketHandler;
import io.github.fernthedev.secondgame.main.ui.MouseHandler;
import io.github.fernthedev.secondgame.main.ui.api.Screen;
import io.github.fernthedev.secondgame.main.ui.screens.EndScreen;
import io.github.fernthedev.secondgame.main.ui.screens.MainMenu;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class Game extends Canvas implements Runnable, IGame {
//    public static final int WIDTHHH = 640, HEIGHTTTT =   WIDTHHH / 12 * 9;
    private static final long serialVersionUID = -7376944666695581278L;

    @Getter
    private static final Logger logger = LoggerFactory.getLogger(Game.class.getName());

    private transient Thread thread;
    private boolean running = true;

//    @Deprecated
//    private static transient Handler handler;

    private static final transient Random r = UniversalHandler.RANDOM;
    private static transient HUD hud;
    private final transient Spawn spawnner;
//    private final Menu menu;
    private final transient KeyInput keyInput;

    private final Window window;

    @Nullable
    @Getter
    private static Screen screen;

    @Getter
    private static Config<Settings> gameSettings;

    @Setter
    @Getter
    private static EntityPlayer mainPlayer;

    private transient JoystickHandler testJoyStick;

    @Getter
    @Setter
    @Nullable
    private static Client client;

    @Getter
    @Nullable
    @Setter
    private static GameServer gameServer;

    @NonNull
    @Getter
    private static NewClientEntityRegistry staticEntityRegistry;



    public static int fern$;

    /**
     * STATE OF PAUSED
     */
    public static boolean paused = false;

    /**
     * DIFFICULTY
     * 0 = NORMAL
     * 1 = HARD
     */
    public int dif = 0;

    //public static int frames = 0;


//    /**
//     * GAME STATES
//     */
//    @Deprecated
//    public enum STATE {
//        MENU,
//        HELP,
//        END,
//        GAME,
//        MULTIPLAYER,
//        HOSTING,
//        GETTING_CONNECT,
//        IN_SERVER,
//        JOINING;
//
//        boolean isGame() {
//            return this == GAME || this == HOSTING || this == IN_SERVER;
//        }
//    }


//    /**
//     * VARIABLE FOR ACCESSING STATES
//     */
//    public static STATE gameState = STATE.MENU;


    /**
     * MAIN
     */
    @SneakyThrows
    private Game() {
        logger.info("Loaded icon");

        UniversalHandler.setIGame(this);
        Settings settings = new Settings();

        settings.setCompressionAlgorithm(CompressionAlgorithm.ZLIB);
        settings.setCompressionLevel(6);

        gameSettings = new GsonConfig<>(settings, new File("./config_settings.json"));
        gameSettings.load();
        gameSettings.save();
        hud = new HUD();

//        handler = new Handler(hud);
        staticEntityRegistry = new NewClientEntityRegistry();

        testJoyStick = new JoystickHandler();


        setScreen(new MainMenu());
//        menu = new io.github.fernthedev.secondgame.main.ui.Menu(this,handler,hud);
        logger.info("LWJGL Version " + Version.getVersion() + " is working.");

        keyInput = new KeyInput(this);

        this.addKeyListener(keyInput);
        this.addMouseListener(new MouseHandler());

        window = new Window(UniversalHandler.WIDTH, UniversalHandler.HEIGHT, "A NEW GAME", this);

        fern$ = r.nextInt(100);


        logger.info("{}", fern$);


        spawnner = new Spawn(hud, this);
    }




    /**
     * RUNNABLE METHOD
     */
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

//        final int TARGET_FPS = 60;
//        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
      //  long lastLoopTime = System.nanoTime();

        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

           // long updateLength = now - lastLoopTime;
           // lastLoopTime = now;

            while(delta >= 1) {
                    tick();
                    delta--;
            }

            if (running)
                render();

            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                logger.info("FPS: " + NumberFormat.getNumberInstance(Locale.US).format(frames));
                frames = 0;
                //updates = 0;
            }



            try {Thread.sleep(UniversalHandler.TICK_WAIT);} catch(InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            //try { Thread.sleep((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000); } catch (InterruptedException e) {e.printStackTrace(); }
        }

    }


    /**
     * TICK METHOD
     */
    private void tick() {
        try {

            if (getScreen() == null) {
                //logger.info("Running thing");

                if (!paused) {

                    //  logger.info(UniversalHandler.running);
                    hud.tick();

                    staticEntityRegistry.tick();
//                    handler.tick();
                    if (gameServer == null && client == null) {
                        spawnner.tick();

                        if (mainPlayer != null && mainPlayer.getHealth() <= 0) {
                            mainPlayer.setHealth(100);
                            setScreen(new EndScreen());
//                        handler.clearEnemies();
//                        int amount = r.nextInt(15);
//                        if (amount < 10) amount = 10;
//                        for (int i = 0; i < amount; i++) {
//                            handler.addObject(new MenuParticle(r.nextInt(WIDTH), r.nextInt(HEIGHT), EntityID.MenuParticle, GameObject.entities));
//                        }
                        }
                    }


//                    if (gameServer != null) {
//
////                        handler.serverTick();
//
//                    }


                    keyInput.tick();
                    testJoyStick.tick();



                }

            } else {
                staticEntityRegistry.tick();
            }

//            handler.tick();
//            testJoyStick.tick();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RENDERING OBJECTS AND GAME
     */
    private void render() {
        if (getBufferStrategy() == null) {
            createBufferStrategy(3);
            return;
        }

        BufferStrategy bs = this.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, UniversalHandler.WIDTH, UniversalHandler.HEIGHT);

        staticEntityRegistry.render(g);
//        handler.render(g);

        if (paused) {
            g.setColor(Color.WHITE);
            g.drawString("Paused", 100, 100);
        }
//        if(gameState == STATE.GAME || gameState == STATE.IN_SERVER || gameState == STATE.HOSTING) {
//            hud.render(g);
//        } else {
        if (screen != null) screen.render(g);
        else hud.render(g);
//        }

        /*else if(gameState == STATE.MENU
                || gameState == STATE.HELP
                || gameState == STATE.END
                || gameState ==  STATE.MULTIPLAYER
                || gameState ==  STATE.JOINING
                || gameState == STATE.GETTING_CONNECT) {
            menu.render(g);
        } */

        g.dispose();
        bs.show();
    }

    /**
     * CREATING A BOX/LIMIT FOR A VARIABLE
     * @param var Variable being affected
     * @param min The minimum value
     * @param max The Max Value
     * @return Returns the max or min if either var is greater than either, if not returns var
     */

    public static float clamp(float var, float min, float max) {
        if(var >= max)
            return max;
        else if(var <= min)
            return min;
        else
            return var;
    }

    /**
     * STARTING METHOD
     */
    public static void main(String[] args) {

        if (Arrays.stream(args).parallel().anyMatch(s -> s.equals("-debug"))) StaticHandler.setDebug(true);

        new Game();
    }


//    public static Handler getHandler() {
//        return handler;
//    }

    /**
     * Threads
     */
    public synchronized void start() {
        thread = new Thread(this, "MainGameThread");
        thread.start();
    }

    public static HUD getHud() {
        return hud;
    }

    /**
     * Stopping game threads
     */
    public synchronized void stop() {

        try {
            UniversalHandler.setRunning(false);
            running = false;
            thread.join();
            System.exit(0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void setupJoiningMultiplayer() {
//        newClientEntityRegistry.startGame();

        getStaticEntityRegistry().clearObjects();


        Client client = new Client(getGameSettings().getConfigData().getHost(), getGameSettings().getConfigData().getPort());
        client.setClientSettingsManager(getGameSettings());
        client.setMaxPacketId(CommonUtil.MAX_PACKET_IDS);
        Game.setClient(client);
        Game.setScreen(null);

        CommonUtil.registerNetworking();

        PacketHandler packetHandler = new PacketHandler();
        client.getPluginManager().registerEvents(packetHandler);
        client.addPacketHandler(packetHandler);
        UUID uuid = UUID.randomUUID();
        StaticHandler.getCore().getLogger().debug("Using uuid name: {}", uuid);
        client.setName(uuid.toString());

        mainPlayer = null;

        Thread thread = new Thread(() -> {
            try {
                client.connect();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
    }


    public static void setupHostingMultiplayer() {
        staticEntityRegistry.startGame();
//        EntityHandler entityHandler = new EntityHandler();
//        UniversalHandler.getInstance().setup(entityHandler);

        staticEntityRegistry.setServerEntityRegistry(new NewServerEntityRegistry());

        GameServer server = new GameServer(new String[0], UniversalHandler.MULTIPLAYER_PORT, staticEntityRegistry.getServerEntityRegistry(), true /* TODO: Remove */);

        Game.setGameServer(server);

        server.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(ServerStartupEvent e) {
                staticEntityRegistry.addEntityObject(Game.mainPlayer);
//                server.getServerGameHandler().getEntityHandler().addEntityObject(Game.getMainPlayer());
            }
        });
    }

    public static void setScreen(@Nullable Screen screen) {
        if (screen != null && screen.isSetParentScreenOnSet()) screen.setParentScreen(Game.screen);
        Game.screen = screen;
    }

    @Override
    public INewEntityRegistry getEntityRegistry() {
        return staticEntityRegistry;
    }

    public Logger getLoggerImpl() {
        return logger;
    }
}
