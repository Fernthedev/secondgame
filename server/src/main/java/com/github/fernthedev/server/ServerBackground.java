package com.github.fernthedev.server;


import com.github.fernthedev.packets.PingPacket;

import java.util.Scanner;
class ServerBackground implements Runnable {

    private final Server server;

    private boolean checked;

    ServerBackground(Server server) {
        this.server = server;
        checked = false;
        System.out.println("Wait for command thread created");
    }


    public void run() {
        Scanner scanner = null;
        if(System.console() != null) {
            scanner = new Scanner(System.in);
            System.out.println("There's a scanner");
        }



        while (server.isRunning()) {
            //if (scanner.hasNextLine()) {
            if (scanner != null) {
                if (!checked) {
                    System.out.println("Type Command:");
                    checked = true;
                }
                String command = scanner.nextLine();
                System.out.println("Executing " + command);
                String[] checkmessage = command.split(" ", 2);


                command = checkmessage[0];

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting");
                    server.shutdownServer();
                    System.exit(0);

                        /*
                        for (Thread thread : Thread.getAllStackTraces().keySet()) {
                            try {
                                //server. ();
                                thread.join();
                                System.exit(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }*/


                    //Main.server.sendObject(new SendMessagePacket(Main.client.player, sendmessage));
                } else if (command.equalsIgnoreCase("ping")) {
                    Server.sendObjectToAllPlayers(new PingPacket());
                }/* else {
                    System.out.println("No scanner ;(");
                    scannerChecked = true;
                }*/
                //}else {
                //      System.out.println("No running");
                //   }
            }
        }
    }
}
