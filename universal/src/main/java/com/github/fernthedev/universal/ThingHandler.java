package com.github.fernthedev.universal;

import com.github.fernthedev.universal.entity.UniversalPlayer;

import java.util.List;
import java.util.Map;

public interface ThingHandler {

     List<GameObject> getGameObjects();

    void setGameObjects(List<GameObject> gameObjects);

    void updatePlayerObject(Object clientPlayer,UniversalPlayer universalPlayer);


    Map<Integer, GameObject> getGameObjectMap();

    void setGameObjectMap(Map<Integer, GameObject> gameObjectMap);


    void addEntityObject(GameObject gameObject);

    void removeEntityObject(GameObject gameObject);

}
