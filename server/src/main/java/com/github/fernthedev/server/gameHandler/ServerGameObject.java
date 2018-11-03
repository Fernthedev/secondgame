package com.github.fernthedev.server.gameHandler;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.ID;
import com.github.fernthedev.universal.entity.*;

import java.awt.*;

public class ServerGameObject extends GameObject {
    public ServerGameObject(float x, float y, ID id,int objectID) {
        super(x, y, id,objectID);
    }

    public static GameObject getObjectType(GameObject gameObject) {
        //if(gameObject.id == ID.SmartEnemy) {
        ///        return new SmartEnemy(gameObject);
        //     }
        switch (gameObject.id) {
            case Player:
                if(!(gameObject instanceof UniversalPlayer))
                    return new UniversalPlayer(gameObject);
                else return gameObject;
            case BasicEnemey:
                if(!(gameObject instanceof BasicEnemy))
                    return new BasicEnemy(gameObject);
                else return gameObject;
            case FastEnemy:
                if(!(gameObject instanceof FastEnemy))
                    return new FastEnemy(gameObject);
                else return gameObject;
            case MenuParticle:
                if(!(gameObject instanceof MenuParticle))
                    return new MenuParticle(gameObject);
                else return gameObject;
            case Trail:
                if(!(gameObject instanceof Trail))
                    return new Trail(gameObject);
                else return gameObject;
            case Coin:
                if(!(gameObject instanceof UniversalCoin))
                    return new UniversalCoin(gameObject);
                else return gameObject;

            default:
                return gameObject;
        }
    }




    public ServerGameObject(GameObject gameObject) {
        super(gameObject);
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
