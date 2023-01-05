package com.github.fernthedev.universal.entity


import com.github.fernthedev.universal.ColorJSON
import com.github.fernthedev.universal.GameObject
import com.github.fernthedev.universal.UniversalHandler
import java.io.Serializable
import java.util.*


class NewGsonGameObject(gameObject: GameObject) : Serializable {

    private var clazz: String
    private var colorJSON: ColorJSON
    var gameObject: ByteArray

    init {
        clazz = gameObject.javaClass.name
        this.gameObject = Base64.getEncoder().encode(UniversalHandler.gson.toJson(gameObject).encodeToByteArray())

        colorJSON = ColorJSON(gameObject.color)
    }

    @Throws(ClassNotFoundException::class)
    fun toGameObject(): GameObject {
        val aClass = Class.forName(clazz)
        return try {
            if (GameObject::class.java.isAssignableFrom(aClass)) {
                val utf8Json = Base64.getDecoder().decode(gameObject).decodeToString()
                val go = UniversalHandler.gson.fromJson(utf8Json, aClass) as GameObject

                go.color = colorJSON.toColor()

                go
            } else throw ClassNotFoundException("Class " + aClass + " is not assignable to " + GameObject::class.java)
        } catch (e: Exception) {
            throw IllegalStateException("The object being decoded is " + aClass.name, e)
        }
    }


}