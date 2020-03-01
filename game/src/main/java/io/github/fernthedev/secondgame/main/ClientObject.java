package io.github.fernthedev.secondgame.main;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.entity.*;
import io.github.fernthedev.secondgame.main.entities.Coin;
import io.github.fernthedev.secondgame.main.entities.SmartEnemy;

import java.awt.*;

public class ClientObject extends GameObject {
        public ClientObject(float x, float y, ID id, int objectID) {
            super(x, y, id,objectID);
        }



        public ClientObject(GameObject gameObject) {
            super(gameObject);
        }

        public static GameObject getObjectType(GameObject gameObject) {
            switch (gameObject.id) {
                case Player:
                    if(!(gameObject instanceof EntityPlayer))
                    return new EntityPlayer(gameObject);
                    else return gameObject;
                case BasicEnemey:
                    if(!(gameObject instanceof BasicEnemy))
                    return new BasicEnemy(gameObject);
                    else return gameObject;
                case Coin:
                    if(!(gameObject instanceof UniversalCoin))
                    return new Coin(gameObject);
                    else return gameObject;
                case FastEnemy:
                    if(!(gameObject instanceof FastEnemy))
                    return new FastEnemy(gameObject);
                    else return gameObject;
                case SmartEnemy:
                    if(!(gameObject instanceof SmartEnemy))
                    return new SmartEnemy(gameObject);
                    else return gameObject;
                case MenuParticle:
                    if(!(gameObject instanceof MenuParticle))
                    return new MenuParticle(gameObject);
                    else return gameObject;
                case Trail:
                    if(!(gameObject instanceof Trail))
                        return new Trail(gameObject);
                        else return gameObject;
                default:
                    return gameObject;
            }
        }

    public static GameObject getObjectType(GsonObject gameObject) {
        switch (gameObject.id) {
            case Player:
                return new EntityPlayer(gameObject);
            case BasicEnemey:
                return new BasicEnemy(gameObject);
            case Coin:
                return new Coin(gameObject);
            case FastEnemy:
                return new FastEnemy(gameObject);
            case SmartEnemy:
                return new SmartEnemy(gameObject);
            case MenuParticle:
                return new MenuParticle(gameObject);
            case Trail:
                return new Trail(gameObject);
            default:
                return gameObject;
        }
    }

        @Override
        public void tick() {
        }

        @Override
        public void render(Graphics g) {
        }

        @Override
        public Rectangle getBounds() {
            return null;
        }
}
