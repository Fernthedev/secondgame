//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.universal.UniversalHandler;

import java.awt.*;

public class HUD {
    private int greenvalue = 255;
    private int score = 0;
    private int level = 1;

    public HUD() {
    }

    public void tick() {
        if(Game.gameState == Game.STATE.GAME) {
            UniversalHandler.mainPlayer.setHealth( (int)Game.clamp((float) UniversalHandler.mainPlayer.getHealth(), 0.0F, 100.0F) );
            //  System.out.println(GAME.mainPlayer + " " + UniversalHandler.mainPlayer.getHealth());
            if (UniversalHandler.mainPlayer != null)
                UniversalHandler.mainPlayer.setHealth(UniversalHandler.mainPlayer.getHealth());

            this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
            this.greenvalue = UniversalHandler.mainPlayer.getHealth() * 2;
            ++this.score;
        }

        if(Game.gameState == Game.STATE.IN_SERVER || Game.gameState == Game.STATE.HOSTING) {
            if(UniversalHandler.mainPlayer != null) {
                UniversalHandler.mainPlayer.setHealth(UniversalHandler.mainPlayer.getHealth());

                UniversalHandler.mainPlayer.setHealth( (int) Game.clamp((float) UniversalHandler.mainPlayer.getHealth(), 0.0F, 100.0F));


                this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
                this.greenvalue = UniversalHandler.mainPlayer.getHealth() * 2;
            }
        }
    }

    public void setCoin(int coins) {
        UniversalHandler.mainPlayer.setCoin(coins);
    }

    public void plusCoin() {
        UniversalHandler.mainPlayer.setCoin(UniversalHandler.mainPlayer.getCoin() +1);
    }

    public int getCoin() {
        return UniversalHandler.mainPlayer.getCoin();
    }

    public void render(Graphics g) {
        if (UniversalHandler.mainPlayer != null) {
            g.setColor(Color.GRAY);
            g.fillRect(15, 15, 200, 32);
            g.setColor(new Color(75, this.greenvalue, 0));
            g.fillRect(15, 15, UniversalHandler.mainPlayer.getHealth() * 2, 32);
            if (UniversalHandler.mainPlayer.getHealth() == 0) {
                g.setColor(Color.RED);
                g.drawRect(15, 15, 200, 32);
            } else {
                g.setColor(Color.WHITE);
                g.drawRect(15, 15, 200, 32);
            }

            g.setColor(Color.WHITE);
            g.drawString("Score: " + this.score, 15, 64);
            g.drawString("Level: " + this.level, 15, 80);
            g.drawString("Coins: " + getCoin(), 15, 96);
        }
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
