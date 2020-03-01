package io.github.fernthedev.secondgame.main.netty.client;


import com.github.fernthedev.exceptions.LostConnectionServer;
import com.github.fernthedev.universal.entity.EntityPlayer;

import java.util.Scanner;

public class Client implements Runnable {

    EntityPlayer player;
    public boolean registered;
    private Scanner scanner;
     final int port;
     final String host;


    public static Thread waitThread;

    static Thread currentThread;

    private static ClientThread clientThread;




    public Client(String host, int port) {
        this.port = port;
        this.host = host;



         clientThread = new ClientThread(this);
         currentThread = new Thread(clientThread);
    }

    public void run() {
        System.out.println("Initializing");

        clientThread.connected = false;
        clientThread.connectToServer = true;
      //  UniversalHandler.running = true;


        while(!clientThread.connected && clientThread.connectToServer) {
            clientThread.connect();


        }
    }

    @Deprecated
    public void initialize() {


    }






    void throwException() {
        try {
            throw new LostConnectionServer(host);
        } catch (LostConnectionServer lostConnectionServer) {
            lostConnectionServer.printStackTrace();
        }
    }

    public static ClientThread getClientThread() {
        return clientThread;
    }

    public void print(Object message) {
        System.out.println(this + " " + message);
    }
}
