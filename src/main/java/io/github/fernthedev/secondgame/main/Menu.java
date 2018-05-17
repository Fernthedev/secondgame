package io.github.fernthedev.secondgame.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Random;

public class Menu extends MouseAdapter {

    private Game game;
    private Handler handler;
    private Random r = new Random();
    private HUD hud;

    public void render(Graphics g) {
        if(Game.gameState == Game.STATE.Menu) {
            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

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
        }else if(Game.gameState == Game.STATE.Help) {
            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Help", 240, 70);

            g.setFont(fnt2);
            g.drawString("Use WASD or arrow keys please",150,200);


            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Back", 270, 390);
        }else if(Game.gameState == Game.STATE.End) {
            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Game Over", 180, 70);

            g.setFont(fnt2);
            g.drawString("Score: " + hud.getScore(),150,200);
            g.drawString("Level: " + hud.getLevel(),150,230);


            g.setFont(fnt2);
            g.drawRect(210, 350, 200, 64);
            g.drawString("Try Again", 245, 390);
        }
    }

    public Menu(Game game,Handler handler,HUD hud) {
        this.hud = hud;
        this.game = game;
        this.handler = handler;
    }

    public void tick() {

    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();


        if (Game.gameState == Game.STATE.Menu) {
            //PLAY BUTTON
            if (mouseOver(mx, my, 210, 150, 200, 64)) {
                Game.gameState = Game.STATE.Game;
                handler.addObject(new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler,hud));
                handler.clearEnemies();
                handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemey, handler));
                return;
            }
            //QUIT
            if (mouseOver(mx, my, 210, 350, 200, 64)) {
                System.exit(0);
            }
            //HELP
            if (mouseOver(mx, my, 210, 250, 200, 64)) {
                Game.gameState = Game.STATE.Help;
            }
        }


        //BACK FOR HELP
        if (Game.gameState == Game.STATE.Help) {
            if (mouseOver(mx, my, 210, 350, 200, 64)) {
                Game.gameState = Game.STATE.Menu;
                return;
            }
        }

        //BACK FOR END
        if (Game.gameState == Game.STATE.End) {
            if (mouseOver(mx, my, 210, 350, 200, 64)) {
                Game.gameState = Game.STATE.Menu;
                hud.setLevel(1);
                hud.setScore(0);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx,int my,int x, int y, int width, int height) {
        return (mx > x && mx < x + width) && (my > y && my < y + height);
    }

}
