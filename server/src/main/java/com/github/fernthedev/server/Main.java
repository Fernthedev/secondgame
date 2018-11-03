package com.github.fernthedev.server;

class Main {

    public static void main(String[] args) {
        Server server = new Server(2000);
        Thread thread = new Thread(server);
        thread.start();
    }

}
