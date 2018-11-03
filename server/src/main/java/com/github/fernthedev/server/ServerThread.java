package com.github.fernthedev.server;

import com.github.fernthedev.packets.Packet;
import com.github.fernthedev.packets.PingPacket;
import com.github.fernthedev.universal.UniversalHandler;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ServerThread implements Runnable {


    private boolean running;

    private boolean isConnected;

    private static final List<ClientPlayer> socketList = new ArrayList<>();

    private final ClientPlayer clientPlayer;



    private final Channel channel;

    private final Thread thread;
    //private ReadListener readListener;

    public ServerThread(Server server, Channel channel, ClientPlayer clientPlayer) {
        this.clientPlayer = clientPlayer;
        thread = Thread.currentThread();
        running = UniversalHandler.running;
        isConnected = true;

        this.channel = channel;
        Server.serverInstanceThreads.add(thread);

    }

    private synchronized void sendObject(Object packet) {
        if (packet instanceof Packet) {
            if (isConnected) {
                channel.writeAndFlush(packet);
                if(!(packet instanceof PingPacket)) {
                  //  System.out.println("Sent " + packet);

                }
            }
        }else {
            System.out.println("not packet");
        }
    }


    synchronized void close(boolean sendObject) {
        try {
            System.out.println("Closing connection at for player " + clientPlayer);
            running = false;
            //DISCONNECT FROM SERVER
            //RemovePlayerPacket packet = new RemovePlayerPacket();
            if(channel != null) {

                if(sendObject && channel.isActive()) {
                    System.out.println("Sent disconnect");
                }

                if ((!channel.isActive())) {
                    //sendObject(new SafeDisconnect());

                    System.out.println("Closing sockets.");

                    //readListener.terminate();
                    //channel.closeFuture();
                    clientPlayer.close();
                    channel.closeFuture().sync();

                    socketList.remove(clientPlayer);



                    System.out.println("Closed sockets ");
                }

            }
            //if(!scanner.nextLine().equals(""))

           /* try {
                throw new LostConnectionServer(clientPlayer.getAdress().toString());
            } catch (LostConnectionServer lostConnectionServer) {
                lostConnectionServer.printStackTrace();
            }*/

            isConnected = false;
            UniversalHandler.threads.remove(thread);
            Server.serverThreads.remove(this);
            System.out.println(thread);
            thread.join();

            //serverSocket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void startListener() {
        /*
        readListener = new ReadListener(clientSocket,out,in);

        listenThread = new Thread(readListener);
        listenThread.start();*/
    }

    private int secondsPassed;

    public void run() {
        if(!running) {
            close(false);
        }

        System.out.println("Checking for " + clientPlayer + " socket " + channel);


        long time = System.nanoTime();

        // And From your main() method or any other method
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
            }
        }, 0, 1000);


        while (isRunning() && clientPlayer.isConnected()) {

            if(secondsPassed >= 5) {
                sendObject(new PingPacket());
                secondsPassed = 0;
               // long nowtime = (System.nanoTime() - time) / 1000000;
               // time = System.nanoTime();
               // System.out.println("Took " + TimeUnit.NANOSECONDS.toMillis(nowtime) + " ms");
            }

            /*try{
                //synchronized (clientSocket) {

                //boolean keepCheck = true;

                    while (in.available() > 0) {

                       // if(in.read() == -1) keepCheck = false;

                        System.out.println("Detected something " + in.available());
                        Object data = in.readObject();
                        System.out.println(data + " was received");
                        listener.received(data, clientSocket.getInetAddress());
                    }

                    //sendObject(new PingPacket());
               // }
            } catch (UnknownHostException e) {
                System.out.println("Lost player from ip.");
            }catch (SocketException e) {
                System.out.println("Lost connection to clientplayer at " + clientPlayer.getAdress());
                //e.printStackTrace();
                close(true,false);
            } catch (ClassNotFoundException | EOFException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                System.out.println("Player timed out");
                out.flush();
                socketList.remove(clientPlayer);

                if(Server.clientNetPlayerList.containsKey(clientPlayer)) {
                    Server.sendObjectToAllPlayers(new RemovePlayerPacket(Server.clientNetPlayerList.get(clientPlayer)));
                }



                clientPlayer.close(false,false);
                close(false,false);
            }*/
        }
    }

    ClientPlayer getPlayer(String adress) {
        for(ClientPlayer clientPlayerThing : socketList) {
            if(clientPlayerThing.getAdress().equals(adress)) return clientPlayerThing;
        }

        return null;
    }

    boolean isRunning() {
        return UniversalHandler.running;
    }

/*
    private class ReadListener implements Runnable {

        private boolean running;

        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        private Thread currentThread;

        public ReadListener(Socket socket,ObjectOutputStream out,ObjectInputStream in) {
            running = true;
            this.socket = socket;
            this.in = in;
            this.out = out;

        }

        void terminate() {
            currentThread = Thread.currentThread();
            running = false;
            try {
                currentThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                clientSocket.setSoTimeout(5000);
                /*
                while (running) {
                    try {

                            //System.out.println("Detected something " + in.available());
                            Object data = in.readObject();

                            if(!(data instanceof PongPacket)) {
                                System.out.println(data + " was received");
                            }

                            listener.received(data, clientSocket.getInetAddress());

                    } catch (UnknownHostException e) {
                        System.out.println("Lost player from ip.");
                    } catch (SocketException e) {
                        System.out.println("Lost connection to clientplayer at " + clientPlayer.getAdress());
                        //e.printStackTrace();
                        close(true, false);
                    } catch (ClassNotFoundException | EOFException e) {
                        e.printStackTrace();
                    } catch (SocketTimeoutException e) {
                        System.out.println("Player timed out");
                        out.flush();
                        socketList.remove(clientPlayer);

                        if (Server.clientNetPlayerList.containsKey(clientPlayer)) {
                            Server.sendObjectToAllPlayers(new RemovePlayerPacket(Server.clientNetPlayerList.get(clientPlayer)));
                        }


                        clientPlayer.close(false, false);
                        close(false, false);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

}
