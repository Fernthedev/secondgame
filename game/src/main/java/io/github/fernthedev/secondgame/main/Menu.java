package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.server.Server;
import com.github.fernthedev.server.gameHandler.EntityHandler;
import com.github.fernthedev.server.gameHandler.ServerGameObject;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.BasicEnemy;
import com.github.fernthedev.universal.entity.UniversalPlayer;
import io.github.fernthedev.secondgame.main.netty.client.Client;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("EmptyMethod")
class Menu extends MouseAdapter {

    private final Handler handler;
    private final Random r = new Random();
    private final HUD hud;

    private static List<MouseOverUI> mouseOverUIList = new ArrayList<>();

    public void render(Graphics g) {
        MouseOverUI button;
        if(Game.gameState == Game.STATE.MENU) {
            mouseOverUIList = new ArrayList<>();
            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("MENU", 240, 70);

            g.setFont(fnt2);
            g.setColor(Color.WHITE);



            button = new MouseOverUI(g,210,150,200,64, Game.STATE.MENU) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.GAME;
                    startGame();
                }
            };

            button.drawString("Play",270,190);
            mouseOverUIList.add(button);



           // g.drawRect(210, 150, 200, 64);
          //  g.drawString("Play", 270, 190);

           // g.drawRect(210, 150, 200, 64);
        //    g.drawString("Helpi", 270, 190);

            button = new MouseOverUI(g, 210, 70, 200, 64, Game.STATE.MENU) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.MULTIPLAYER;
                }
            };
            button.drawString("MULTIPLAYER",230,110);
            mouseOverUIList.add(button);


            button = new MouseOverUI(g,210,250,200,64, Game.STATE.MENU) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.HELP;
                }
            };

            button.drawString("HELP",270,290);
            mouseOverUIList.add(button);

           // g.drawRect(210, 250, 200, 64);
       //     g.drawString("HELP", 270, 290);

            button = new MouseOverUI(g,210,350,200,64, Game.STATE.MENU) {
                @Override
                void onClick() {
                    System.exit(0);
                }
            };
            button.drawString("Quit",270,390);
            mouseOverUIList.add(button);
        //    g.drawRect(210, 350, 200, 64);
        //    g.drawString("Quit", 270, 390);


        }else if(Game.gameState == Game.STATE.HELP) {
            mouseOverUIList = new ArrayList<>();

            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("HELP", 240, 70);

            g.setFont(fnt2);
            g.drawString("Use WASD or arrow keys please",150,200);


            g.setFont(fnt2);

            button = new MouseOverUI(g,210,350,200,64, Game.STATE.HELP) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.MENU;
                }
            };
            button.drawString("Back",270,390);
            mouseOverUIList.add(button);

           // /g.drawRect(210, 350, 200, 64);
          //  g.drawString("Back", 270, 390);
        }else if(Game.gameState == Game.STATE.END) {
            mouseOverUIList = new ArrayList<>();

            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("GAME Over", 180, 70);

            g.setFont(fnt2);
            g.drawString("Score: " + hud.getScore(),150,200);
            g.drawString("Level: " + hud.getLevel(),150,230);
            g.drawString("Coin: " + Game.getHud().getCoin(),150,260);




            button = new MouseOverUI(g,210,350,200,64, Game.STATE.END) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.MENU;
                    hud.setLevel(1);
                    hud.setScore(0);
                }
            };

            button.drawString("Try again",245,390);

            mouseOverUIList.add(button);
           // g.drawRect(210, 350, 200, 64);
        //    g.drawString("Try Again", 245, 390);


        }else if (Game.gameState == Game.STATE.MULTIPLAYER) {
            mouseOverUIList = new ArrayList<>();

            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("MULTIPLAYER", 240, 70);

            g.setFont(fnt2);


            button = new MouseOverUI(g,210,250,200,64,Game.STATE.MULTIPLAYER) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.JOINING;
                    startGame();

                    Client client = new Client("localhost",2000);
                    Thread thread = new Thread(client);

                    thread.start();
                    UniversalHandler.threads.add(thread);
                }
            };

            button.drawString("Join");

            button = new MouseOverUI(g,210,350,200,64, Game.STATE.MULTIPLAYER) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.HOSTING;
                    System.out.println(Game.gameState + "\n");
                    startGame();
                    EntityHandler entityHandler = new EntityHandler();
                    UniversalHandler.getInstance().setup(entityHandler);

                    UniversalHandler.isServer = true;

                    Server server = new Server(2000,entityHandler);
                    server.setPlayerStarter(ServerGameObject.getObjectType(new ServerGameObject(UniversalHandler.mainPlayer)));

                    Thread thread = new Thread(server);
                    thread.start();
                    UniversalHandler.threads.add(thread);

                    System.out.println(UniversalHandler.threads.size() + " threads");
                    //UniversalHandler.getThingHandler().addEntityObject();
                   // server.startServer();
                }
            };
            button.drawString("Host",270,390);
            //mouseOverUIList.add(button);

        } else if(Game.gameState == Game.STATE.JOINING) {

        }
    }

    public Menu(Game game,Handler handler,HUD hud) {
        this.hud = hud;
        this.handler = handler;
    }

    public void tick() {

    }

    private void startGame() {

        if(Game.gameState != Game.STATE.JOINING) {
            UniversalPlayer player = new UniversalPlayer(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, GameObject.entities);
            UniversalHandler.mainPlayer = player;

            handler.addObject(player);
        }


            handler.clearObjects();

        if(Game.gameState == Game.STATE.JOINING) {
            UniversalHandler.isServer = true;
        }
      //  handler.clearEnemies();

        if(Game.gameState == Game.STATE.GAME) {
            UniversalHandler.isServer = false;
            handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemey, GameObject.entities));
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        //System.out.println(GAME.gameState);
        for(MouseOverUI mouseOverUI : mouseOverUIList) {
            if(mouseOverUI.state == Game.gameState) {
                boolean result = mouseOver(mx, my, mouseOverUI);

                if (result) {
                    //System.out.println("Clicked ui element at state " + mouseOverUI.state + " " + mouseOverUI);
                    mouseOverUI.onClick();
                    System.out.println(Game.gameState + "\n");
                    return;
                }
            }
        }

/*
            if (GAME.gameState == GAME.STATE.MENU) {
                //PLAY BUTTON
                if (mouseOver(mx, my, 210, 150, 200, 64)) {

                }
                //QUIT
                if (mouseOver(mx, my, 210, 350, 200, 64)) {

                }
                //HELP
                if (mouseOver(mx, my, 210, 250, 200, 64)) {

                }
            }


            //BACK FOR HELP
            if (GAME.gameState == GAME.STATE.HELP) {
                if (mouseOver(mx, my, 210, 350, 200, 64)) {

                }
            }*/

            //BACK FOR END
           /* if (GAME.gameState == GAME.STATE.END) {
                if (mouseOver(mx, my, 210, 350, 200, 64)) {

                }
            }*/
    }



    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mx,int my,int x, int y, int width, int height) {
        return (mx > x && mx < x + width) && (my > y && my < y + height);
    }

    private boolean mouseOver(int mx, int my, MouseOverUI ui) {
        return (mx > ui.x && mx < ui.x + ui.width) && (my > ui.y && my < ui.y + ui.height);
    }

    private abstract class MouseOverUI {
        private final int x;
        private final int width;
        private final int y;
        private final int height;

        private String string = "";
        private int stringX,stringY;

        private final Game.STATE state;

        private final Graphics g;

        public int getX() {
            return x;
        }

        public int getWidth() {
            return width;
        }

        public int getY() {
            return y;
        }

        public int getHeight() {
            return height;
        }

        MouseOverUI(Graphics g, int x, int y, int width, int height, Game.STATE state) {
            this.x = x;
            this.width = width;
            this.y = y;
            this.height = height;
            this.state = state;
            this.g = g;

            g.drawRect(x, y, width, height);
            mouseOverUIList.add(this);
        }

        void drawString(String string, int stringX, int stringY) {
            g.drawString(string,stringX,stringY);
            this.string = string;
            this.stringX = stringX;
            this.stringY = stringY;
        }

        void drawString(String string) {
            g.drawString(string,x+60,y+40);
            this.string = string;
            this.stringX = x+60;
            this.stringY = y+40;
        }

        abstract void onClick();


        @Override
        public String toString() {

            return string + " " + stringX + " " + stringY + " box " + x + " " + y + " " + width + ":" + height;
        }
    }
}
