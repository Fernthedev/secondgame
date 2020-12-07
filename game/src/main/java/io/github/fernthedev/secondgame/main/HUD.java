//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class HUD {
    private int greenvalue = 255;
    private int score = 0;
    private int level = 1;

    public HUD() {
    }

    public void tick() {

        if (Game.getMainPlayer() != null) {
            Game.getMainPlayer().setHealth((int) Game.clamp((float) Game.getMainPlayer().getHealth(), 0.0F, 100.0F));
            //  System.out.println(GAME.mainPlayer + " " + Game.mainPlayer.getHealth());
            Game.getMainPlayer().setHealth(Game.getMainPlayer().getHealth());


            this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
            this.greenvalue = Game.getMainPlayer().getHealth() * 2;
            this.score++;
        }


//        if(Game.gameState == Game.STATE.GAME) {
//            Game.mainPlayer.setHealth( (int)Game.clamp((float) Game.mainPlayer.getHealth(), 0.0F, 100.0F) );
//            //  System.out.println(GAME.mainPlayer + " " + Game.mainPlayer.getHealth());
//            if (Game.mainPlayer != null)
//                Game.mainPlayer.setHealth(Game.mainPlayer.getHealth());
//
//            this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
//            this.greenvalue = Game.mainPlayer.getHealth() * 2;
//            this.score++;
//        }
//
//        if(Game.gameState == Game.STATE.IN_SERVER || Game.gameState == Game.STATE.HOSTING) {
//            if(Game.mainPlayer != null) {
//                Game.mainPlayer.setHealth(Game.mainPlayer.getHealth());
//
//                Game.mainPlayer.setHealth( (int) Game.clamp((float) Game.mainPlayer.getHealth(), 0.0F, 100.0F));
//
//
//                this.greenvalue = (int) Game.clamp((float) this.greenvalue, 0.0F, 255.0F);
//                this.greenvalue = Game.mainPlayer.getHealth() * 2;
//            }
//        }
    }

    public void render(Graphics g) {
        if (Game.getMainPlayer() != null) {
            g.setColor(Color.GRAY);
            g.fillRect(15, 15, 200, 32);
            g.setColor(new Color(75, this.greenvalue, 0));
            g.fillRect(15, 15, Game.getMainPlayer().getHealth() * 2, 32);
            if (Game.getMainPlayer().getHealth() == 0) {
                g.setColor(Color.RED);
                g.drawRect(15, 15, 200, 32);
            } else {
                g.setColor(Color.WHITE);
                g.drawRect(15, 15, 200, 32);
            }

            g.setColor(Color.WHITE);
            g.drawString("Score: " + this.score, 15, 64);
            g.drawString("Level: " + this.level, 15, 80);
            g.drawString("Coins: " + Game.getMainPlayer().getCoin(), 15, 96);
            if (Game.getClient() != null) {
                try {
                    g.drawString("Ping: " + Game.getClient().getPingTime(TimeUnit.MILLISECONDS) + "ms", 15, 112);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
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
