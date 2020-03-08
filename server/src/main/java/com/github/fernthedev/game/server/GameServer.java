package com.github.fernthedev.game.server;


import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.IGame;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.core.ColorCode;
import com.github.fernthedev.core.api.event.api.EventHandler;
import com.github.fernthedev.core.api.event.api.Listener;
import com.github.fernthedev.exceptions.DebugException;
import com.github.fernthedev.game.server.game_handler.GameNetworkProcessingHandler;
import com.github.fernthedev.game.server.game_handler.ServerGameHandler;
import com.github.fernthedev.server.Server;
import com.github.fernthedev.server.event.ServerShutdownEvent;
import com.github.fernthedev.server.event.ServerStartupEvent;
import com.github.fernthedev.terminal.server.ServerTerminal;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.Getter;

public class GameServer extends ServerTerminal implements IGame {

    @Getter
    private GameNetworkProcessingHandler processHandler;

    @Getter
    private ServerGameHandler serverGameHandler;

    @Getter
    private Thread serverThread;



    public GameServer(String[] args, int port, NewServerEntityRegistry entityHandler) {
        entityHandler.setServer(this);
        new DebugException().printStackTrace();
        allowPortArgParse = false;
        allowChangePassword = false;
        allowTermPackets = false;
        lightAllowed = false;
        ServerTerminal.port = port;

        if (UniversalHandler.getIGame() == null) UniversalHandler.setIGame(this);

        ServerTerminal.main(args);

        server.addPacketHandler(new ServerPacketHandler(this));

        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(ServerStartupEvent event) {
                UniversalHandler.setRunning(true);
                Server.getLogger().info("Running game startup code");

                CommonUtil.registerPackets();

                serverThread = server.getServerThread();
                processHandler = new GameNetworkProcessingHandler(GameServer.this);
                server.addChannelHandler(processHandler);
                server.getPluginManager().registerEvents(processHandler);

                serverGameHandler = new ServerGameHandler(GameServer.this, entityHandler);
                Thread thread = new Thread(serverGameHandler);
                thread.start();
                System.out.println(thread + " is from " + serverGameHandler);


            }

            @EventHandler
            public void onEvent(ServerShutdownEvent e) {
                UniversalHandler.setRunning(false);

            }
        });
    }

    public Server getServer() {
        return server;
    }

    public static void main(String[] args) {
        new GameServer(args, UniversalHandler.MULTIPLAYER_PORT, new NewServerEntityRegistry());

        server.addShutdownListener(() -> {
            Server.getLogger().info(ColorCode.RED + "Goodbye!");
            System.exit(0);
        });
    }

    @Override
    public INewEntityRegistry getEntityRegistry() {
        return serverGameHandler.getEntityHandler();
    }
}
