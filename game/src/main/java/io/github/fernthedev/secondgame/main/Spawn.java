//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.BasicEnemy;
import com.github.fernthedev.universal.entity.Coin;
import com.github.fernthedev.universal.entity.FastEnemy;
import com.github.fernthedev.universal.entity.SmartEnemy;
import io.github.fernthedev.secondgame.main.logic.NewClientEntityRegistry;

import java.util.Random;

class Spawn {

    private final HUD hud;
    private final Game game;

    private int scoreKeep = 0;
    private final Random r = UniversalHandler.RANDOM;
    private int timer;
    private int nexttimer;

    public Spawn(HUD hud, Game game) {
        this.hud = hud;
        this.game = game;
    }

    public void tick() {
        NewClientEntityRegistry handler = game.getStaticEntityRegistry();
        scoreKeep++;
        int coinspawn = hud.getScore() + r.nextInt(512);
        if (hud.getScore() == coinspawn) {
            Game.getStaticEntityRegistry().addEntityObject(
                    new Coin(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.COIN));
            //handler.addObject(new Coin(r.nextInt(GAME.WIDTH - 50), r.nextInt(GAME.HEIGHT - 50), EntityID.Coin, GameObject.entities));
        }

        if (scoreKeep >= 250) {

            hud.setLevel(hud.getLevel() + 1);
            timer++;
            scoreKeep = 0;

            int mob = r.nextInt(4);
            if(mob == 0 ) mob++;

            //System.out.println(mob);
            if (mob == 1) {
                handler.addEntityObject(new BasicEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.ENEMY));
            }

            if (mob == 2) {
                handler.addEntityObject(new FastEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.ENEMY));
            }

            if (mob == 3) {
                handler.addEntityObject(new SmartEnemy(r.nextInt(UniversalHandler.WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.ENEMY, Game.getMainPlayer()));
            }
        }

        if (timer >= nexttimer) {
            int min = 7;
            int max = 15;
            nexttimer = r.nextInt(max - min) + min;
            timer = 0;
            handler.resetLevel();
            scoreKeep = 250;
            hud.setLevel(hud.getLevel() - 1);
        }

    }
}
