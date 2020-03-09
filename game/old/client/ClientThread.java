package io.github.fernthedev.secondgame.main.deprecated.netty.client;

import com.github.fernthedev.universal.UniversalHandler;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.deprecated.netty.client.netty.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

@Deprecated
public class ClientThread implements Runnable {

    private boolean isRegistered() {
        return client.registered;
    }


    //boolean running = false;

     boolean connected;


    private final EventListener listener;
    private final Client client;

    boolean connectToServer;

    private Thread readingThread;

    private ChannelFuture future;
    private Channel channel;

    private EventLoopGroup workerGroup;
    //private ReadListener readListener;


    public ClientThread(Client client) {
        this.client = client;
        listener = new EventListener(client);
        UniversalHandler.running = true;
    }

    void connect() {
        System.out.println("Connecting to server.");

        workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ObjectEncoder(),
                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                            new ClientHandler(client, listener));
                }
            });

            b.option(ChannelOption.TCP_NODELAY,true);
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);


            future = b.connect(client.host, client.port).sync();
            channel = future.channel();
            //future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        if (future.isSuccess() && future.channel().isActive()) {
            System.out.println("SOCKET CONNECTED!");
            Game.gameState = Game.STATE.IN_SERVER;




            Game.getHandler().clearObjects();

            // System.out.println("Making new thread");


            //socket.getOutputStream().flush();


            //BufferedReader bis = new BufferedReader(new InputStreamReader(in));
            connected = true;


           /* if (!Client.currentThread.isAlive()) {
                UniversalHandler.running = true;
                Client.currentThread.start();
                System.out.println("This thread started");
            }*/

            //setReadListener();

            /*
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (channel.isActive() && player != null) {
                    //sendObject(new RemovePlayerPacket(player));
                    disconnect();
                }
                for (Thread thread : Thread.getAllStackTraces().keySet()) {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));*/

        }
    }



    public void sendObject(Object packet) {
        if (packet instanceof Packet) {
            if (channel.isActive()) {
                channel.writeAndFlush(packet);


                // System.out.println("Sent");
              /*  if(!(packet instanceof PongPacket)) {
                    System.out.println("Sent an gameObjects which is " + packet);
                }*/
            } else{
                disconnect();
            }
        }else {
            System.out.println("not packet");
        }
    }

    public void disconnect() {
        System.out.println("Disconnecting from server");
        try {

           // future.channel().closeFuture().sync();
            workerGroup.shutdownGracefully();
            Thread.currentThread().join();
            System.out.println("Disconnected");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    private void close() {
        try {
            client.print("Closing connection.");


            //DISCONNECT FROM SERVER
            if(channel.isActive()) {

                if(channel.isActive()) {
                    //in.close();
                    // out.close();
                    channel.closeFuture().sync();
                    client.print("Closed connection.");
                }
            }

            for(Thread thread : Thread.getAllStackTraces().keySet()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Closing client!");
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void run() {
        //client.print(running);
        client.print("Checking for " + client.host + ":" + client.port + " socket " + channel);
        while (UniversalHandler.running) {
            /*
            try {
                //client.print("checking");
                    if (!isRegistered() && socket.isConnected()) {
                        sendObject(new ConnectedPacket(client.name));
                        client.registered = true;
                    }
                    if(in.available() != 0) {
                        client.print(in.available());
                    }


                   // boolean keepCheck = true;

                while (in.available() > 0) {

                   // if(in.read() == -1) {keepCheck = false;}

                    System.out.println("Something in the mailbox");
                            Object data = in.readObject();
                            if (data == null) {
                                sendObject(new NullClass());
                            } else {
                                client.print("Recieved");
                                listener.received(data);
                            }
                        }



                //  }
            } catch (UnknownHostException e) {
                disconnect();
            } catch (SocketException e) {
                e.printStackTrace();
                close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                client.print("LOST CONNECTION TO SERVER! RETRYING!");
                client.initialize();
                connected = false;
                //throwException();

            } catch (ClassCastException e) {
                e.printStackTrace();
                sendObject(new NullClass());
            } catch (EOFException e) {
                sendObject(new PlayerLeave());
                e.printStackTrace();
            } finally {
                client.print(running);
                client.print(Thread.getAllStackTraces());
                client.print(Thread.currentThread());
                client.print(Client.currentThread);
                client.print(Thread.currentThread().equals(Client.currentThread));
            }*/
        }

    }

    void setReadListener() {
        //readListener = new ReadListener(socket,in,out);

        //readingThread = new Thread(readListener);
        //readingThread.start();
    }


   /* private class ReadListener implements Runnable {

        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        private boolean running = false;

        public ReadListener(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            this.socket = socket;
            this.in = in;
            this.out = out;
            running = true;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    try {
                        //client.print("checking");
                        if (!isRegistered() && socket.isConnected()) {
                            sendObject(new ConnectedPacket(client.name));
                            client.registered = true;
                        }

                        // boolean keepCheck = true;


                            // if(in.read() == -1) {keepCheck = false;}

                            //System.out.println("Something in the mailbox");
                        synchronized (in) {
                                Object data = in.readObject();

                                if (data == null) {
                                    synchronized (out) {
                                        sendObject(new NullClass());
                                    }
                                } else {
                                    if (!(data instanceof PingPacket)) {
                                        client.print("Recieved");
                                    }
                                    listener.received(data);
                                }

                        }

                        //  }
                    } catch (StreamCorruptedException e) {

                        e.printStackTrace();
                        //sendObject(new NullClass());

                    }catch (UnknownHostException e) {
                        disconnect();
                    } catch (SocketException e) {
                        e.printStackTrace();
                        close();
                    } catch (ClassNotFoundException | EOFException e) {
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        client.print("LOST CONNECTION TO SERVER! RETRYING!");
                        client.initialize();
                        connected = false;
                        //throwException();

                    } catch (ClassCastException e) {
                        e.printStackTrace();
                        sendObject(new NullClass());
                    } /* finally {
                    client.print(running);
                    client.print(Thread.getAllStackTraces());
                    client.print(Thread.currentThread());
                    client.print(Client.currentThread);
                    client.print(Thread.currentThread().equals(Client.currentThread));
                }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
