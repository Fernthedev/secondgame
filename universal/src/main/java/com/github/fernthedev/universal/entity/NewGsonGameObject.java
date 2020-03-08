package com.github.fernthedev.universal.entity;

import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewGsonGameObject implements Serializable {

    public NewGsonGameObject(GameObject gameObject) {
        this.clazz = gameObject.getClass().getName();
        this.gameObject = UniversalHandler.gson.toJson(gameObject);
    }

    /**
     * Defines that this is an object to remove
     */
    protected NewGsonGameObject() {
        clazz = null;
        gameObject = null;
    }

    protected String clazz;
    protected String gameObject;

    public GameObject toGameObject() throws ClassNotFoundException {
        if (this.clazz == null || gameObject == null) {
            return null;
        }
        Class<?> aClass = Class.forName(this.clazz);

        try {
            if (GameObject.class.isAssignableFrom(aClass)) {
                return (GameObject) UniversalHandler.gson.fromJson(gameObject, aClass);
            } else throw new ClassNotFoundException("Class " + aClass + " is not assignable to " + GameObject.class);
        } catch (Exception e) {
            throw new IllegalStateException("The object being decoded is " + aClass.getName(), e);
        }
    }

    public static NewGsonGameObject nullObject() {
        return new NewGsonGameObject();
    }

}
