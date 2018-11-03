//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.Color;
import java.awt.Graphics;

public class HUD {
    public static int HEALTH = 100;
    private int greenvalue = 255;
    private int score = 0;
    private int level = 1;
    public int coin = 0;

    public HUD() {
    }

    public void tick() {
        if(Game.gameState == Game.STATE.Game) {
            HEALTH = (int) Game.clamp((float) HEALTH, 0.0F, 100.0F);
            //  System.out.println(Game.mainPlayer + " " + HEALTH);
            if (Game.mainPlayer != null)
                Game.mainPlayer.setHealth(HEALTH);

            this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
            this.greenvalue = HEALTH * 2;
            ++this.score;
        }

        if(Game.gameState == Game.STATE.InServer || Game.gameState == Game.STATE.Hosting) {
            if(Game.mainPlayer != null) {
                HEALTH = Game.mainPlayer.getHealth();

                HEALTH = (int) Game.clamp((float) HEALTH, 0.0F, 100.0F);


                this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
                this.greenvalue = HEALTH * 2;
            }
        }
    }

    public void setCoin(int coins) {
        this.coin = coins;
    }

    public void plusCoin() {
        coin++;
    }

    public int getCoin() {
        return coin;
    }

    public void render(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(15, 15, 200, 32);
        g.setColor(new Color(75, this.greenvalue, 0));
        g.fillRect(15, 15, HEALTH * 2, 32);
        if (HEALTH == 0) {
            g.setColor(Color.RED);
            g.drawRect(15, 15, 200, 32);
        } else {
            g.setColor(Color.WHITE);
            g.drawRect(15, 15, 200, 32);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + this.score, 15, 64);
        g.drawString("Level: " + this.level, 15, 80);
        g.drawString("Coins: " + coin, 15, 96);
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
