package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.server.Server;
import com.github.fernthedev.server.gameHandler.EntityHandler;
import com.github.fernthedev.server.gameHandler.ServerGameObject;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.BasicEnemy;
import io.github.fernthedev.secondgame.main.entities.Player;
import io.github.fernthedev.secondgame.main.netty.client.Client;
import io.github.fernthedev.secondgame.main.netty.client.ClientEntityHandler;
import io.github.fernthedev.secondgame.main.netty.client.ServerClientObject;

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
        if(Game.gameState == Game.STATE.Menu) {
            mouseOverUIList = new ArrayList<>();
            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Menu", 240, 70);

            g.setFont(fnt2);
            g.setColor(Color.WHITE);



            button = new MouseOverUI(g,210,150,200,64, Game.STATE.Menu) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Game;
                    startGame();
                }
            };

            button.drawString("Play",270,190);
            mouseOverUIList.add(button);



           // g.drawRect(210, 150, 200, 64);
          //  g.drawString("Play", 270, 190);

           // g.drawRect(210, 150, 200, 64);
        //    g.drawString("Helpi", 270, 190);

            button = new MouseOverUI(g, 210, 70, 200, 64, Game.STATE.Menu) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Multiplayer;
                }
            };
            button.drawString("Multiplayer",230,110);
            mouseOverUIList.add(button);


            button = new MouseOverUI(g,210,250,200,64, Game.STATE.Menu) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Help;
                }
            };

            button.drawString("Help",270,290);
            mouseOverUIList.add(button);

           // g.drawRect(210, 250, 200, 64);
       //     g.drawString("Help", 270, 290);

            button = new MouseOverUI(g,210,350,200,64, Game.STATE.Menu) {
                @Override
                void onClick() {
                    System.exit(0);
                }
            };
            button.drawString("Quit",270,390);
            mouseOverUIList.add(button);
        //    g.drawRect(210, 350, 200, 64);
        //    g.drawString("Quit", 270, 390);


        }else if(Game.gameState == Game.STATE.Help) {
            mouseOverUIList = new ArrayList<>();

            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Help", 240, 70);

            g.setFont(fnt2);
            g.drawString("Use WASD or arrow keys please",150,200);


            g.setFont(fnt2);

            button = new MouseOverUI(g,210,350,200,64, Game.STATE.Help) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Menu;
                }
            };
            button.drawString("Back",270,390);
            mouseOverUIList.add(button);

           // /g.drawRect(210, 350, 200, 64);
          //  g.drawString("Back", 270, 390);
        }else if(Game.gameState == Game.STATE.End) {
            mouseOverUIList = new ArrayList<>();

            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Game Over", 180, 70);

            g.setFont(fnt2);
            g.drawString("Score: " + hud.getScore(),150,200);
            g.drawString("Level: " + hud.getLevel(),150,230);
            g.drawString("Coin: " + Game.getHud().coin,150,260);




            button = new MouseOverUI(g,210,350,200,64, Game.STATE.End) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Menu;
                    hud.setLevel(1);
                    hud.setScore(0);
                }
            };

            button.drawString("Try again",245,390);

            mouseOverUIList.add(button);
           // g.drawRect(210, 350, 200, 64);
        //    g.drawString("Try Again", 245, 390);


        }else if (Game.gameState == Game.STATE.Multiplayer) {
            mouseOverUIList = new ArrayList<>();

            Font fnt = new Font("arial", Font.BOLD, 50);
            Font fnt2 = new Font("arial", Font.BOLD, 30);

            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Multiplayer", 240, 70);

            g.setFont(fnt2);


            button = new MouseOverUI(g,210,250,200,64,Game.STATE.Multiplayer) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Joining;
                    startGame();

                    Client client = new Client("localhost",2000);
                    Thread thread = new Thread(client);

                    thread.start();
                    UniversalHandler.threads.add(thread);
                }
            };

            button.drawString("Join");

            button = new MouseOverUI(g,210,350,200,64, Game.STATE.Multiplayer) {
                @Override
                void onClick() {
                    Game.gameState = Game.STATE.Hosting;
                    System.out.println(Game.gameState + "\n");
                    startGame();
                    EntityHandler entityHandler = new EntityHandler();
                    UniversalHandler.getInstance().setup(entityHandler);

                    Game.setServerClientObject(new ServerClientObject());




                    Server server = new Server(2000,entityHandler);
                    server.setPlayerStarter(ServerGameObject.getObjectType(new ServerGameObject(Game.mainPlayer)));

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

        } else if(Game.gameState == Game.STATE.Joining) {

        }
    }

    public Menu(Game game,Handler handler,HUD hud) {
        this.hud = hud;
        this.handler = handler;
    }

    public void tick() {

    }

    private void startGame() {

        if(Game.gameState != Game.STATE.Joining) {
            Player player = new Player(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, handler, hud, GameObject.entities);
            Game.mainPlayer = player;

            handler.addObject(player);
        }


            handler.clearObjects();

        if(Game.gameState == Game.STATE.Joining) {
            UniversalHandler.getInstance().setup(new ClientEntityHandler());

        }
      //  handler.clearEnemies();

        if(Game.gameState == Game.STATE.Game) {

            UniversalHandler.getInstance().setup(new ClientEntityHandler());
            handler.addObject(new BasicEnemy(r.nextInt(Game.WIDTH - 50), r.nextInt(Game.HEIGHT - 50), ID.BasicEnemey, GameObject.entities));
        }
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        //System.out.println(Game.gameState);
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
            if (Game.gameState == Game.STATE.Menu) {
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
            if (Game.gameState == Game.STATE.Help) {
                if (mouseOver(mx, my, 210, 350, 200, 64)) {

                }
            }*/

            //BACK FOR END
           /* if (Game.gameState == Game.STATE.End) {
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
