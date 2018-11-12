package com.github.fernthedev.server;

import com.github.fernthedev.exceptions.NonPacketException;
import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.server.gameHandler.EntityHandler;
import com.github.fernthedev.server.gameHandler.ServerGameHandler;
import com.github.fernthedev.server.netty.ProcessingHandler;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable {

    private final int port;

    public int getPort() {
        return port;
    }

    //private Socket clientSocket;
    //private ServerSocket serverSocket;
    public static final int WIDTH = 640,HEIGHT =  WIDTH / 12*9;

    //private boolean running = false;

    // private ObjectOutputStream out;
   // private ObjectInputStream in;


    public static final Map<Channel,ClientPlayer> socketList = new HashMap<>();

    public static final Map<Channel,Server> channelServerHashMap = new HashMap<>();

    static final List<ServerThread> serverThreads = new ArrayList<>();

    static final List<Thread> serverInstanceThreads = new ArrayList<>();

    private GameObject starterPlayer = null;
    private Thread serverBackgroundThread = null;

    public void setPlayerStarter(GameObject gameObject) {
        if(gameObject instanceof UniversalPlayer) {
            starterPlayer = gameObject;
        }
    }

    public GameObject getStarterPlayer() {
        return starterPlayer;
    }

    Object lastPacket;

    private ChannelFuture future;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup,workerGroup;

    private EntityHandler entityHandler;

    private ProcessingHandler processingHandler;

    public Server(int port) {
        this.port = port;
        UniversalHandler.running = true;
       // running = true;
    }

    public Server(int port,EntityHandler entityHandler) {
        this.port = port;
        this.entityHandler = entityHandler;
    }


    @Deprecated
    public void startServer() {
    }

    private void bind() {
        try {

           // while(port <= 0) {
          ///      port = r.nextInt(2000);
        //    }

            future = bootstrap.bind(port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (!future.isSuccess()) {
         //   port = r.nextInt(2000);
       //     bind();
            System.out.println("Failed to bind port");
        }else{
            System.out.println("Binded port on " + future.channel().localAddress());
            System.out.println(UniversalHandler.running);
        }
    }



    private void connect() {
        while (UniversalHandler.running) {
            try {


                future = future.await().sync();

                future.channel().closeFuture().sync();


                if(future.channel().isActive() && future.channel().isRegistered()) {
                    channelServerHashMap.put(future.channel(),this);
                }

                /*

                future.addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();


                        System.out.println("Connected to channel " + future.channel());
                        establishClient(future);

                        channelFuture.channel().closeFuture().addListener((ChannelFutureListener) channelFuture1 -> {
                            future.channel().closeFuture().sync();
                            channel.closeFuture().sync();
                        });

                    }
                });*/

                //future.channel().closeFuture().sync();
                //future.channel().closeFuture().sync();


            /*if (!future.isSuccess()) {
                System.out.println("Failed to bind port");
            }*/

                // if (future.channel().isRegistered()) {
                //         System.out.println("Connected to channel " + future.channel().remoteAddress());
                //    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }

    public synchronized static void sendObjectToAllPlayers(Packet packet) {
        for(Channel channel : socketList.keySet()) {
          //  System.out.println(packet);
            ClientPlayer clientPlayer = socketList.get(channel);

            //System.out.println(clientPlayer);

            if (packet != null) {
               // if (clientPlayer.channel.isActive()) {
                    clientPlayer.sendObject(packet);
            //    }
                clientPlayer.setLastPacket(packet);
            } else {
                System.out.println("not packet");
                try {
                    throw new NonPacketException(null);
                } catch (NonPacketException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    /*
    @Deprecated
    private void establishClient(ChannelFuture channelFuture) {

        if (channel != null) {
            connected = true;

            ClientPlayer clientPlayer = new ClientPlayer(channelFuture);

            System.out.println("Created clientPlayer variable " + clientPlayer + " channel " + future.channel());

            socketList.put(channelFuture.channel(),clientPlayer);


            listener = new EventListener(this, clientPlayer);
            //processingHandler.setListener(listener);
            System.out.println("Events registered");


// And From your main() method or any other method
            FernThread runningFernThread;


            ServerThread serverThread = new ServerThread(this, channelFuture, clientPlayer, listener);

            runningFernThread = new FernThread(serverThread);
            clientPlayer.setThread(runningFernThread);

            runningFernThread.startThread();
            serverThread.startListener();

            System.out.println("Thread started for player " + clientPlayer);


            clientPlayer.sendObject(new RequestNamePacket());

            Runtime.getRuntime().addShutdownHook(new FernThread() {
                @Override
                public void run() {
                    for (ServerThread serverThread : serverThreads) {
                        if (serverThread.clientPlayer.channel.isOpen()) {
                            System.out.println("Gracefully shutting down/");
                            sendObjectToAllPlayers(new LostServerConnectionPacket());
                            serverThread.clientPlayer.close(false, false);
                        }
                    }
                }
            });
        }else{
            System.out.println("Channel is null");
            throw new NullPointerException();
        }
    }*/


    synchronized void shutdownServer() {
        UniversalHandler.isServer = false;
        UniversalHandler.running = false;
        UniversalHandler.threads.remove(Thread.currentThread());
        for (ServerThread thread : serverThreads) {
            thread.close(true);
        }

        try {
            for (Thread threade : UniversalHandler.threads) {
                if(threade != null) {
                    System.out.println("Closing " + threade.getName());
                    threade.join();
                }
            }
            System.out.println("Done, now groups.");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            System.out.println("Now current thread.");
            Thread.currentThread().join();
            System.out.println("Goodbye!");
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public boolean isRunning() {
        return UniversalHandler.running;
      //  return running;
    }

    @Override
    public void run() {
        //serverSocket = new ServerSocket(port);

        if(entityHandler == null)  {entityHandler = new EntityHandler();
        UniversalHandler.getInstance().setup(entityHandler);
        }

        bossGroup = new NioEventLoopGroup();
        processingHandler = new ProcessingHandler(this);
        workerGroup = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {

                        ch.pipeline().addLast(new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                processingHandler);
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);


                 /*finally {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }*/

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownServer));

        UniversalHandler.isServer = true;
       // UniversalHandler.running = true;
        System.out.println("Server socket registered");
        //  new Thread(new ServerBackground(this)).start();
        //Timer pingPongTimer = new Timer("pingpong");
        System.out.println("Server started successfully at localhost");

        //serverPlayer = new NetPlayer(0,"Server");
        // PlayerHandler.players.put(serverPlayer.id,serverPlayer);

        //connect();
        ServerGameHandler serverGameHandler = new ServerGameHandler(this, entityHandler);
       Thread thread = new Thread(serverGameHandler);
       thread.start();
       System.out.println(thread + " is from " + serverGameHandler);
       UniversalHandler.threads.add(thread);


       if(System.console() != null) {
           ServerBackground background = new ServerBackground(this);
           serverBackgroundThread = new Thread(background);
           serverBackgroundThread.start();
           System.out.println(serverBackgroundThread + " is from " + background);
           UniversalHandler.threads.add(serverBackgroundThread);
       }


        System.out.println(UniversalHandler.threads.size() + " threads");

        bind();
        connect();
    }


}
