package com.github.fernthedev.universal.entity


import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.UniversalHandler
import java.io.Serializable


class NewGsonGameObject : Serializable {
    constructor(gameObject: GameObject) {
        clazz = gameObject.javaClass.name
        this.gameObject = UniversalHandler.gson.toJson(gameObject)
    }

    /**
     * Defines that this is an object to remove
     */
    protected constructor() {
        clazz = null
        gameObject = null
    }

    protected var clazz: String?
    protected var gameObject: String?
    @Throws(ClassNotFoundException::class)
    fun toGameObject(): GameObject? {
        if (clazz == null || gameObject == null) {
            return null
        }
        val aClass = Class.forName(clazz)
        return try {
            if (GameObject::class.java.isAssignableFrom(aClass)) {
                UniversalHandler.gson.fromJson(gameObject, aClass) as GameObject
            } else throw ClassNotFoundException("Class " + aClass + " is not assignable to " + GameObject::class.java)
        } catch (e: Exception) {
            throw IllegalStateException("The object being decoded is " + aClass.name, e)
        }
    }

    companion object {
        @JvmStatic
        fun nullObject(): NewGsonGameObject {
            return NewGsonGameObject()
        }
    }
}