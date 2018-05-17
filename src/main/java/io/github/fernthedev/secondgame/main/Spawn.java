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
        ++this.scoreKeep;
        this.coinspawn = this.hud.getScore() + this.r.nextInt(512);
        if (this.hud.getScore() == this.coinspawn) {
            this.handler.addObject(new Coin(this.r.nextInt(590), this.r.nextInt(427), ID.Coin, this.handler));
        }

        if (this.scoreKeep >= 250) {
            this.hud.setLevel(this.hud.getLevel() + 1);
            ++this.timer;
            this.scoreKeep = 0;

            int mob;
            for(mob = this.r.nextInt(4); mob <= 0; mob = this.r.nextInt(4)) {
                ;
            }

            System.out.println(mob);
            if (mob == 1) {
                this.handler.addObject(new BasicEnemy(this.r.nextInt(590), this.r.nextInt(427), ID.BasicEnemey, this.handler));
            }

            if (mob == 2) {
                this.handler.addObject(new FastEnemy(this.r.nextInt(590), this.r.nextInt(427), ID.FastEnemy, this.handler));
            }

            if (mob == 3) {
                this.handler.addObject(new SmartEnemy((float)this.r.nextInt(590), (float)this.r.nextInt(427), ID.SmartEnemy, this.handler));
            }
        }

        if (this.timer >= this.nexttimer) {
            this.nexttimer = this.r.nextInt(15) + 7;
            this.timer = 0;
            this.handler.clearEnemies();
            this.scoreKeep = 250;
        }

    }
}
