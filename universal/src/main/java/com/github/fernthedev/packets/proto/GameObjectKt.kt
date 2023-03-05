//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: packets.proto

package com.github.fernthedev.packets.proto;

@kotlin.jvm.JvmName("-initializegameObject")
public inline fun gameObject(block: com.github.fernthedev.packets.proto.GameObjectKt.Dsl.() -> kotlin.Unit): com.github.fernthedev.packets.proto.Packets.GameObject =
  com.github.fernthedev.packets.proto.GameObjectKt.Dsl._create(com.github.fernthedev.packets.proto.Packets.GameObject.newBuilder()).apply { block() }._build()
public object GameObjectKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.github.fernthedev.packets.proto.Packets.GameObject.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.github.fernthedev.packets.proto.Packets.GameObject.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.github.fernthedev.packets.proto.Packets.GameObject = _builder.build()

    /**
     * <code>.com.github.fernthedev.packets.proto.Location location = 1;</code>
     */
    public var location: com.github.fernthedev.packets.proto.Packets.Location
      @JvmName("getLocation")
      get() = _builder.getLocation()
      @JvmName("setLocation")
      set(value) {
        _builder.setLocation(value)
      }
    /**
     * <code>.com.github.fernthedev.packets.proto.Location location = 1;</code>
     */
    public fun clearLocation() {
      _builder.clearLocation()
    }
    /**
     * <code>.com.github.fernthedev.packets.proto.Location location = 1;</code>
     * @return Whether the location field is set.
     */
    public fun hasLocation(): kotlin.Boolean {
      return _builder.hasLocation()
    }

    /**
     * <code>.com.github.fernthedev.packets.proto.EntityID entityId = 2;</code>
     */
    public var entityId: com.github.fernthedev.packets.proto.Packets.EntityID
      @JvmName("getEntityId")
      get() = _builder.getEntityId()
      @JvmName("setEntityId")
      set(value) {
        _builder.setEntityId(value)
      }
    /**
     * <code>.com.github.fernthedev.packets.proto.EntityID entityId = 2;</code>
     */
    public fun clearEntityId() {
      _builder.clearEntityId()
    }

    /**
     * <code>float velX = 3;</code>
     */
    public var velX: kotlin.Float
      @JvmName("getVelX")
      get() = _builder.getVelX()
      @JvmName("setVelX")
      set(value) {
        _builder.setVelX(value)
      }
    /**
     * <code>float velX = 3;</code>
     */
    public fun clearVelX() {
      _builder.clearVelX()
    }

    /**
     * <code>float velY = 4;</code>
     */
    public var velY: kotlin.Float
      @JvmName("getVelY")
      get() = _builder.getVelY()
      @JvmName("setVelY")
      set(value) {
        _builder.setVelY(value)
      }
    /**
     * <code>float velY = 4;</code>
     */
    public fun clearVelY() {
      _builder.clearVelY()
    }

    /**
     * <code>.com.github.fernthedev.packets.proto.UUID uniqueId = 5;</code>
     */
    public var uniqueId: com.github.fernthedev.packets.proto.Packets.UUID
      @JvmName("getUniqueId")
      get() = _builder.getUniqueId()
      @JvmName("setUniqueId")
      set(value) {
        _builder.setUniqueId(value)
      }
    /**
     * <code>.com.github.fernthedev.packets.proto.UUID uniqueId = 5;</code>
     */
    public fun clearUniqueId() {
      _builder.clearUniqueId()
    }
    /**
     * <code>.com.github.fernthedev.packets.proto.UUID uniqueId = 5;</code>
     * @return Whether the uniqueId field is set.
     */
    public fun hasUniqueId(): kotlin.Boolean {
      return _builder.hasUniqueId()
    }

    /**
     * <code>.com.github.fernthedev.packets.proto.Color color = 6;</code>
     */
    public var color: com.github.fernthedev.packets.proto.Packets.Color
      @JvmName("getColor")
      get() = _builder.getColor()
      @JvmName("setColor")
      set(value) {
        _builder.setColor(value)
      }
    /**
     * <code>.com.github.fernthedev.packets.proto.Color color = 6;</code>
     */
    public fun clearColor() {
      _builder.clearColor()
    }
    /**
     * <code>.com.github.fernthedev.packets.proto.Color color = 6;</code>
     * @return Whether the color field is set.
     */
    public fun hasColor(): kotlin.Boolean {
      return _builder.hasColor()
    }

    /**
     * <code>float width = 7;</code>
     */
    public var width: kotlin.Float
      @JvmName("getWidth")
      get() = _builder.getWidth()
      @JvmName("setWidth")
      set(value) {
        _builder.setWidth(value)
      }
    /**
     * <code>float width = 7;</code>
     */
    public fun clearWidth() {
      _builder.clearWidth()
    }

    /**
     * <code>float height = 8;</code>
     */
    public var height: kotlin.Float
      @JvmName("getHeight")
      get() = _builder.getHeight()
      @JvmName("setHeight")
      set(value) {
        _builder.setHeight(value)
      }
    /**
     * <code>float height = 8;</code>
     */
    public fun clearHeight() {
      _builder.clearHeight()
    }

    /**
     * <code>bool hasTrail = 9;</code>
     */
    public var hasTrail: kotlin.Boolean
      @JvmName("getHasTrail")
      get() = _builder.getHasTrail()
      @JvmName("setHasTrail")
      set(value) {
        _builder.setHasTrail(value)
      }
    /**
     * <code>bool hasTrail = 9;</code>
     */
    public fun clearHasTrail() {
      _builder.clearHasTrail()
    }

    /**
     * <pre>
     * player
     * </pre>
     *
     * <code>string name = 10;</code>
     */
    public var name: kotlin.String
      @JvmName("getName")
      get() = _builder.getName()
      @JvmName("setName")
      set(value) {
        _builder.setName(value)
      }
    /**
     * <pre>
     * player
     * </pre>
     *
     * <code>string name = 10;</code>
     */
    public fun clearName() {
      _builder.clearName()
    }

    /**
     * <code>int32 health = 11;</code>
     */
    public var health: kotlin.Int
      @JvmName("getHealth")
      get() = _builder.getHealth()
      @JvmName("setHealth")
      set(value) {
        _builder.setHealth(value)
      }
    /**
     * <code>int32 health = 11;</code>
     */
    public fun clearHealth() {
      _builder.clearHealth()
    }

    /**
     * <code>int32 coin = 12;</code>
     */
    public var coin: kotlin.Int
      @JvmName("getCoin")
      get() = _builder.getCoin()
      @JvmName("setCoin")
      set(value) {
        _builder.setCoin(value)
      }
    /**
     * <code>int32 coin = 12;</code>
     */
    public fun clearCoin() {
      _builder.clearCoin()
    }
  }
}
@kotlin.jvm.JvmSynthetic
public inline fun com.github.fernthedev.packets.proto.Packets.GameObject.copy(block: com.github.fernthedev.packets.proto.GameObjectKt.Dsl.() -> kotlin.Unit): com.github.fernthedev.packets.proto.Packets.GameObject =
  com.github.fernthedev.packets.proto.GameObjectKt.Dsl._create(this.toBuilder()).apply { block() }._build()

val com.github.fernthedev.packets.proto.Packets.GameObjectOrBuilder.locationOrNull: com.github.fernthedev.packets.proto.Packets.Location?
  get() = if (hasLocation()) getLocation() else null

val com.github.fernthedev.packets.proto.Packets.GameObjectOrBuilder.uniqueIdOrNull: com.github.fernthedev.packets.proto.Packets.UUID?
  get() = if (hasUniqueId()) getUniqueId() else null

val com.github.fernthedev.packets.proto.Packets.GameObjectOrBuilder.colorOrNull: com.github.fernthedev.packets.proto.Packets.Color?
  get() = if (hasColor()) getColor() else null
