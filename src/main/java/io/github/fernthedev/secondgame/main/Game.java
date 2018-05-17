//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import io.github.fernthedev.secondgame.main.Game.STATE;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas implements Runnable {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 477;
    private static final long serialVersionUID = -7016397730883067077L;
    private Thread thread;
    private Boolean running = false;
    private Handler handler;
    private Random r;
    private HUD hud;
    private Spawn spawnner;
    private Menu menu;
    public static int fern$;
    public static boolean paused = false;
    /**
     * GAME STATES
     */
    public enum STATE {
        Menu,
        Help,
        Select,
        End,
        Game
    }
    public static STATE gameState;
    public static BufferedImage sprite_sheet;

    public Game() {
        BufferedImageLoader loader = new BufferedImageLoader();
        sprite_sheet = loader.loadImage("/icon.png");
        System.out.println("Loaded icon");
        this.hud = new HUD();
        this.handler = new Handler(this.hud);
        this.menu = new Menu(this, this.handler, this.hud);
        this.addKeyListener(new KeyInput(this.handler, this));
        this.addMouseListener(this.menu);
        new Window(640, 477, "A NEW GAME", this);
        this.r = new Random();
        fern$ = this.r.nextInt(100) + 70;
        System.out.println(fern$);
        this.spawnner = new Spawn(this.handler, this.hud, this);
        if (gameState == STATE.Game) {
            this.handler.addObject(new Player(288, 206, ID.Player, this.handler, this.hud));
            this.handler.addObject(new BasicEnemy(this.r.nextInt(590), this.r.nextInt(427), ID.BasicEnemey, this.handler));
        } else {
            int amount = this.r.nextInt(15);
            if (amount < 10) {
                amount = 10;
            }

            for(int i = 0; i < amount; ++i) {
                this.handler.addObject(new MenuParticle(this.r.nextInt(640), this.r.nextInt(477), ID.MenuParticle, this.handler));
            }
        }

    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0D;
        double ns = 1.0E9D / amountOfTicks;
        double delta = 0.0D;
        long timer = System.currentTimeMillis();
        int var11 = 0;

        while(this.running) {
            long now = System.nanoTime();
            delta += (double)(now - lastTime) / ns;

            for(lastTime = now; delta >= 1.0D; --delta) {
                this.tick();
            }

            if (this.running) {
                this.render();
            }

            ++var11;
            if (System.currentTimeMillis() - timer > 1000L) {
                timer += 1000L;
                var11 = 0;
            }
        }

        this.stop();
    }

    private void tick() {
        if (gameState == STATE.Game) {
            if (!paused) {
                this.handler.tick();
                this.hud.tick();
                this.spawnner.tick();
                if (HUD.HEALTH <= 0) {
                    HUD.HEALTH = 100;
                    gameState = STATE.End;
                    this.handler.clearEnemies();
                    int amount = this.r.nextInt(15);
                    if (amount < 10) {
                        amount = 10;
                    }

                    for(int i = 0; i < amount; ++i) {
                        this.handler.addObject(new MenuParticle(this.r.nextInt(640), this.r.nextInt(477), ID.MenuParticle, this.handler));
                    }
                }
            }
        } else if (gameState == STATE.Menu || gameState == STATE.End) {
            this.menu.tick();
            this.handler.tick();
        }

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
        } else {
            Graphics g = bs.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 640, 477);
            this.handler.render(g);
            if (paused) {
                g.setColor(Color.WHITE);
                g.drawString("Paused", 100, 100);
            }

            if (gameState == STATE.Game) {
                this.hud.render(g);
            } else if (gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.End) {
                this.menu.render(g);
            }

            g.dispose();
            bs.show();
        }
    }

    public static float clamp(float var, float min, float max) {
        if (var >= max) {
            return max;
        } else {
            return var <= min ? min : var;
        }
    }

    public static void main(String[] args) {
        new Game();
    }

    public synchronized void start() {
        this.thread = new Thread(this);
        this.thread.start();
        this.running = true;
    }

    public synchronized void stop() {
        try {
            this.thread.join();
            this.running = false;
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    static {
        gameState = STATE.Menu;
        sprite_sheet = null;
    }
}
