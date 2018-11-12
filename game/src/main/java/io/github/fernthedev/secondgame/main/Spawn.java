//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.BasicEnemy;
import com.github.fernthedev.universal.entity.FastEnemy;
import io.github.fernthedev.secondgame.main.entities.Coin;
import io.github.fernthedev.secondgame.main.entities.SmartEnemy;

import java.util.Random;

class Spawn {

    private final Handler handler;
    private final HUD hud;
    private int scoreKeep = 0;
    private final Random r = new Random();
    private int timer;
    private int nexttimer;

    public Spawn(Handler handler, HUD hud, Game game) {
        this.handler = handler;
        this.hud = hud;
    }

    public void tick() {
        scoreKeep++;
        int coinspawn = hud.getScore() + r.nextInt(512);
        if (hud.getScore() == coinspawn) {
            UniversalHandler.getThingHandler().addEntityObject(
                    new Coin(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.Coin, GameObject.entities));
            //handler.addObject(new Coin(r.nextInt(GAME.WIDTH - 50), r.nextInt(GAME.HEIGHT - 50), ID.Coin, GameObject.entities));
        }

        if (scoreKeep >= 250) {
            hud.setLevel(hud.getLevel() + 1);
            timer++;
            scoreKeep = 0;

            int mob = r.nextInt(4);
            if(mob == 0 ) mob++;

            //System.out.println(mob);
            if (mob == 1) {
                handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemey, GameObject.entities));
            }

            if (mob == 2) {
                handler.addObject(new FastEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.FastEnemy, GameObject.entities));
            }

            if (mob == 3) {
                handler.addObject(new SmartEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.SmartEnemy, GameObject.entities,UniversalHandler.mainPlayer));
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
