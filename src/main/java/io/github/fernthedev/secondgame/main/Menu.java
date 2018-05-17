//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.github.fernthedev.secondgame.main;

import io.github.fernthedev.secondgame.main.Game.STATE;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Menu extends MouseAdapter {
    private Game game;
    private Handler handler;
    private Random r = new Random();
    private HUD hud;

    public void render(Graphics g) {
        Font fnt;
        Font fnt2;
        if (Game.gameState == STATE.Menu) {
            fnt = new Font("arial", 1, 50);
            fnt2 = new Font("arial", 1, 30);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Menu", 240, 70);
            g.setFont(fnt2);
            g.setColor(Color.WHITE);
            g.drawRect(210, 150, 200, 64);
            g.drawString("Play", 270, 190);
            g.drawRect(210, 250, 200, 64);
            g.drawString("Help", 270, 290);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Quit", 270, 390);
        } else if (Game.gameState == STATE.Help) {
            fnt = new Font("arial", 1, 50);
            fnt2 = new Font("arial", 1, 30);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Help", 240, 70);
            g.setFont(fnt2);
            g.drawString("Use WASD keys please", 150, 200);
            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Back", 270, 390);
        } else if (Game.gameState == STATE.End) {
            fnt = new Font("arial", 1, 50);
            fnt2 = new Font("arial", 1, 30);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Game Over", 180, 70);
            g.setFont(fnt2);
            g.drawString("Score: " + this.hud.getScore(), 150, 200);
            g.drawString("Level: " + this.hud.getLevel(), 150, 230);
            g.drawString("Coins: " + HUD.coin, 150, 260);
            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Try Again", 245, 390);
        }

    }

    public Menu(Game game, Handler handler, HUD hud) {
        this.hud = hud;
        this.game = game;
        this.handler = handler;
    }

    public void tick() {
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if (Game.gameState == STATE.Menu) {
            if (this.mouseOver(mx, my, 210, 150, 200, 64)) {
                Game.gameState = STATE.Game;
                this.handler.addObject(new Player(288, 206, ID.Player, this.handler, this.hud));
                this.handler.clearEnemies();
                this.handler.addObject(new BasicEnemy(this.r.nextInt(590), this.r.nextInt(427), ID.BasicEnemey, this.handler));
                return;
            }

            if (this.mouseOver(mx, my, 210, 350, 200, 64)) {
                System.exit(0);
            }

            if (this.mouseOver(mx, my, 210, 250, 200, 64)) {
                Game.gameState = STATE.Help;
            }
        }

        if (Game.gameState == STATE.Help && this.mouseOver(mx, my, 210, 350, 200, 64)) {
            Game.gameState = STATE.Menu;
        } else {
            if (Game.gameState == STATE.End && this.mouseOver(mx, my, 210, 350, 200, 64)) {
                Game.gameState = STATE.Menu;
                this.hud.setLevel(1);
                this.hud.setScore(0);
            }

        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return mx > x && mx < x + width && my > y && my < y + height;
    }
}
