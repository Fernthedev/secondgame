package com.github.fernthedev.game.server;


import com.github.fernthedev.CommonUtil;
import com.github.fernthedev.IGame;
import com.github.fernthedev.exceptions.DebugException;
import com.github.fernthedev.fernutils.console.ArgumentArrayUtils;
import com.github.fernthedev.game.server.game_handler.GameNetworkProcessingHandler;
import com.github.fernthedev.game.server.game_handler.ServerGameHandler;
import com.github.fernthedev.lightchat.core.ColorCode;
import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.lightchat.core.api.event.api.EventHandler;
import com.github.fernthedev.lightchat.core.api.event.api.Listener;
import com.github.fernthedev.lightchat.server.SenderInterface;
import com.github.fernthedev.lightchat.server.Server;
import com.github.fernthedev.lightchat.server.event.ServerShutdownEvent;
import com.github.fernthedev.lightchat.server.event.ServerStartupEvent;
import com.github.fernthedev.lightchat.server.terminal.ServerTerminal;
import com.github.fernthedev.lightchat.server.terminal.ServerTerminalSettings;
import com.github.fernthedev.lightchat.server.terminal.command.Command;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public class GameServer extends ServerTerminal implements IGame {

    @Getter
    private GameNetworkProcessingHandler processHandler;

    @Getter
    private ServerGameHandler serverGameHandler;

    @Getter
    private Thread serverThread;

    public GameServer(String[] args, int defaultPort, NewServerEntityRegistry entityHandler) {
        entityHandler.setServer(this);
        new DebugException().printStackTrace();

        if (UniversalHandler.getIGame() == null) UniversalHandler.setIGame(this);

        AtomicInteger port = new AtomicInteger(defaultPort);

        ArgumentArrayUtils.parseArguments(args)
                .handle("-port", queue -> {

                    try {
                        port.set(Integer.parseInt(queue.remove()));
                        if (port.get() <= 0) {
                            logger.error("-port cannot be less than 0");
                            port.set(-1);
                        } else logger.info("Using port {}", port);
                    } catch (NumberFormatException e) {
                        logger.error("-port is not a number");
                        port.set(-1);
                    }



                })
                .handle("-debug", queue -> StaticHandler.setDebug(true))
                .apply();

        init(args,
                ServerTerminalSettings.builder()
                        .port(port.get())
                        .allowChangePassword(false)
                        .allowTermPackets(false)
                        .build());



        server.setMaxPacketId(CommonUtil.MAX_PACKET_IDS);

        server.addPacketHandler(new ServerPacketHandler(this));

        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEvent(ServerStartupEvent event) {
                UniversalHandler.setRunning(true);
                server.getLogger().info("Running game startup code");

                CommonUtil.registerNetworking();

                serverThread = server.getServerThread();
                processHandler = new GameNetworkProcessingHandler(GameServer.this);
                server.addChannelHandler(processHandler);
                server.getPluginManager().registerEvents(processHandler);

                serverGameHandler = new ServerGameHandler(GameServer.this, entityHandler);
                Thread thread = new Thread(serverGameHandler);
                thread.start();

                registerCommand(new Command("start") {
                    @Override
                    public void onCommand(SenderInterface sender, String[] args) {
                         server.getAuthenticationManager().authenticate(sender).thenAccept(aBoolean -> {
                            if (aBoolean) {
                                getServerGameHandler().setStarted(true);
                            }
                        });


                    }
                });

                registerCommand(new Command("stop") {
                    @Override
                    public void onCommand(SenderInterface sender, String[] args) {
                        server.getAuthenticationManager().authenticate(sender).thenAccept(aBoolean -> {
                            if (aBoolean) {
                                getServerGameHandler().setStarted(false);
                                getEntityRegistry().removeRespawnAllPlayers();
                            }
                        });
                    }
                });


            }

            @EventHandler
            public void onEvent(ServerShutdownEvent e) {
                UniversalHandler.setRunning(false);

            }
        });
        startBind();

    }

    public Server getServer() {
        return server;
    }

    public static void main(String[] args) {
        new GameServer(args, UniversalHandler.MULTIPLAYER_PORT, new NewServerEntityRegistry());

        server.addShutdownListener(() -> {
            server.getLogger().info(ColorCode.RED + "Goodbye!");
            System.exit(0);
        });
    }

    @Override
    public NewServerEntityRegistry getEntityRegistry() {
        return serverGameHandler.getEntityHandler();
    }
}
