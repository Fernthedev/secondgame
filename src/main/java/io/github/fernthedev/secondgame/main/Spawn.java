//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.util.Random;

public class Spawn {

    private Handler handler;
    private HUD hud;
    private int scoreKeep = 0;
    private Random r = new Random();
    private Game game;
    private int coinspawn;
    private int timer;
    private int nexttimer;

    public Spawn(Handler handler, HUD hud, Game game) {
        this.game = game;
        this.handler = handler;
        this.hud = hud;
    }

    public void tick() {
        scoreKeep++;
        coinspawn = hud.getScore() + r.nextInt(512);
        if (hud.getScore() == coinspawn) {
            handler.addObject(new Coin(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.Coin, handler));
        }

        if (scoreKeep >= 250) {
            hud.setLevel(hud.getLevel() + 1);
            timer++;
            scoreKeep = 0;

            int mob = r.nextInt(4);

            System.out.println(mob);
            if (mob == 1) {
                handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemey, handler));
            }

            if (mob == 2) {
                handler.addObject(new FastEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.FastEnemy, handler));
            }

            if (mob == 3) {
                handler.addObject(new SmartEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.SmartEnemy, handler));
            }
        }

        if (timer >= nexttimer) {
            nexttimer = r.nextInt(15) + 7;
            timer = 0;
            handler.clearEnemies();
            scoreKeep = 250;
            hud.setLevel(hud.getLevel() - 1);
        }

    }
}
