package com.github.fernthedev;

import com.github.fernthedev.universal.UniversalHandler;

public abstract class TickRunnable implements Runnable {

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(UniversalHandler.isRunning()) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                //System.out.println(UniversalHandler.running);
                //updates++;
                delta--;
            }

            try {
                Thread.sleep(UniversalHandler.TICK_WAIT);
            } catch(InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    protected abstract void tick();

}
