//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: packets.proto

package com.github.fernthedev.packets.proto;

@kotlin.jvm.JvmName("-initializesendObjectsListPacket")
public inline fun sendObjectsListPacket(block: com.github.fernthedev.packets.proto.SendObjectsListPacketKt.Dsl.() -> kotlin.Unit): com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket =
  com.github.fernthedev.packets.proto.SendObjectsListPacketKt.Dsl._create(com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket.newBuilder()).apply { block() }._build()
public object SendObjectsListPacketKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  public class Dsl private constructor(
    private val _builder: com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket.Builder
  ) {
    public companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket = _builder.build()

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    public class ObjectListProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <pre>
     *&#47; If null, deleted
     * </pre>
     *
     * <code>map&lt;string, .com.github.fernthedev.packets.proto.GameObjectNullable&gt; objectList = 1;</code>
     */
     public val objectList: com.google.protobuf.kotlin.DslMap<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable, ObjectListProxy>
      @kotlin.jvm.JvmSynthetic
      @JvmName("getObjectListMap")
      get() = com.google.protobuf.kotlin.DslMap(
        _builder.getObjectListMap()
      )
    /**
     * <pre>
     *&#47; If null, deleted
     * </pre>
     *
     * <code>map&lt;string, .com.github.fernthedev.packets.proto.GameObjectNullable&gt; objectList = 1;</code>
     */
    @JvmName("putObjectList")
    public fun com.google.protobuf.kotlin.DslMap<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable, ObjectListProxy>
      .put(key: kotlin.String, value: com.github.fernthedev.packets.proto.Packets.GameObjectNullable) {
         _builder.putObjectList(key, value)
       }
    /**
     * <pre>
     *&#47; If null, deleted
     * </pre>
     *
     * <code>map&lt;string, .com.github.fernthedev.packets.proto.GameObjectNullable&gt; objectList = 1;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("setObjectList")
    @Suppress("NOTHING_TO_INLINE")
    public inline operator fun com.google.protobuf.kotlin.DslMap<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable, ObjectListProxy>
      .set(key: kotlin.String, value: com.github.fernthedev.packets.proto.Packets.GameObjectNullable) {
         put(key, value)
       }
    /**
     * <pre>
     *&#47; If null, deleted
     * </pre>
     *
     * <code>map&lt;string, .com.github.fernthedev.packets.proto.GameObjectNullable&gt; objectList = 1;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("removeObjectList")
    public fun com.google.protobuf.kotlin.DslMap<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable, ObjectListProxy>
      .remove(key: kotlin.String) {
         _builder.removeObjectList(key)
       }
    /**
     * <pre>
     *&#47; If null, deleted
     * </pre>
     *
     * <code>map&lt;string, .com.github.fernthedev.packets.proto.GameObjectNullable&gt; objectList = 1;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("putAllObjectList")
    public fun com.google.protobuf.kotlin.DslMap<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable, ObjectListProxy>
      .putAll(map: kotlin.collections.Map<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable>) {
         _builder.putAllObjectList(map)
       }
    /**
     * <pre>
     *&#47; If null, deleted
     * </pre>
     *
     * <code>map&lt;string, .com.github.fernthedev.packets.proto.GameObjectNullable&gt; objectList = 1;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("clearObjectList")
    public fun com.google.protobuf.kotlin.DslMap<kotlin.String, com.github.fernthedev.packets.proto.Packets.GameObjectNullable, ObjectListProxy>
      .clear() {
         _builder.clearObjectList()
       }

    /**
     * <code>.com.github.fernthedev.packets.proto.GameObject mainPlayer = 2;</code>
     */
    public var mainPlayer: com.github.fernthedev.packets.proto.Packets.GameObject
      @JvmName("getMainPlayer")
      get() = _builder.getMainPlayer()
      @JvmName("setMainPlayer")
      set(value) {
        _builder.setMainPlayer(value)
      }
    /**
     * <code>.com.github.fernthedev.packets.proto.GameObject mainPlayer = 2;</code>
     */
    public fun clearMainPlayer() {
      _builder.clearMainPlayer()
    }
    /**
     * <code>.com.github.fernthedev.packets.proto.GameObject mainPlayer = 2;</code>
     * @return Whether the mainPlayer field is set.
     */
    public fun hasMainPlayer(): kotlin.Boolean {
      return _builder.hasMainPlayer()
    }

    /**
     * <code>bool teleport = 3;</code>
     */
    public var teleport: kotlin.Boolean
      @JvmName("getTeleport")
      get() = _builder.getTeleport()
      @JvmName("setTeleport")
      set(value) {
        _builder.setTeleport(value)
      }
    /**
     * <code>bool teleport = 3;</code>
     */
    public fun clearTeleport() {
      _builder.clearTeleport()
    }
  }
}
@kotlin.jvm.JvmSynthetic
public inline fun com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket.copy(block: com.github.fernthedev.packets.proto.SendObjectsListPacketKt.Dsl.() -> kotlin.Unit): com.github.fernthedev.packets.proto.Packets.SendObjectsListPacket =
  com.github.fernthedev.packets.proto.SendObjectsListPacketKt.Dsl._create(this.toBuilder()).apply { block() }._build()

val com.github.fernthedev.packets.proto.Packets.SendObjectsListPacketOrBuilder.mainPlayerOrNull: com.github.fernthedev.packets.proto.Packets.GameObject?
  get() = if (hasMainPlayer()) getMainPlayer() else null
