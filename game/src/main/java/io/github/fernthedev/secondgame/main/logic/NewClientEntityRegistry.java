package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.exceptions.DebugException;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.*;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.entities.CoinRenderer;
import io.github.fernthedev.secondgame.main.entities.MenuParticle;
import io.github.fernthedev.secondgame.main.entities.Trail;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;

public class NewClientEntityRegistry extends INewEntityRegistry {


    public NewClientEntityRegistry() {
        DefaultEntityRenderer defaultEntityRenderer = new DefaultEntityRenderer();
        registerEntity(BasicEnemy.class, defaultEntityRenderer);
        registerEntity(FastEnemy.class, defaultEntityRenderer);
        registerEntity(SmartEnemy.class, defaultEntityRenderer);
        registerEntity(EntityPlayer.class,  defaultEntityRenderer);
        registerEntity(MenuParticle.class, defaultEntityRenderer);
        registerEntity(Trail.class, new Trail.Renderer());


        registerEntity(Coin.class, new CoinRenderer());
    }

    @Nullable
    @Getter
    @Setter
    private NewServerEntityRegistry serverEntityRegistry;

    private static final Map<Class<? extends GameObject>, IEntityRenderer<? extends GameObject>> RENDER_REGISTRY = new HashMap<>();

    public <T extends GameObject> void registerEntity(Class<T> gameObject, IEntityRenderer<? extends GameObject> renderer) {
        RENDER_REGISTRY.put(gameObject, renderer);
    }

    public <T extends GameObject> IEntityRenderer<T> getRenderer(Class<T> clazz) {
        if (!RENDER_REGISTRY.containsKey(clazz)) throw new IllegalArgumentException("The GameObject " + clazz.getName() + " does not have a renderer registered or assigned.");
        return (IEntityRenderer<T>) RENDER_REGISTRY.get(clazz);
    }


    @Override
    public Map<UUID, GameObject> getGameObjects() {
        if (getEntityRegistryInUse() == this) return gameObjects;
        return getEntityRegistryInUse().getGameObjects();
    }

    private INewEntityRegistry getEntityRegistryInUse() {
        if (Game.getGameServer() != null) return serverEntityRegistry;
        else return this;
    }

    @Override
    public void collisionCheck(EntityPlayer universalPlayer) {
        if (Game.getClient() == null && Game.getGameServer() == null)
            super.collisionCheck(universalPlayer);
    }

    @Override
    protected String clampAndTP(GameObject gameObject) {
        if (Game.getClient() == null && Game.getGameServer() == null)
            return super.clampAndTP(gameObject);
        else if (gameObject != null && Game.getMainPlayer() != null && gameObject.getUniqueId() == Game.getMainPlayer().getUniqueId())
            return super.clampAndTP(gameObject);


        return null;
    }

    @Override
    protected void onEntityUpdate() {
//        for (GameObject gameObject : new ArrayList<>(gameObjects.values()
//                .parallelStream()
//                .filter(gameObject -> !(gameObject instanceof Trail))
//                .collect(Collectors.toList()))
//        ) {

        new ArrayList<>(gameObjects.values())
                .parallelStream()
                .filter(gameObject -> !(gameObject instanceof Trail))
                .forEach(gameObject -> {
                    try {
//                        if (gameObject.getVelX() != 0 || gameObject.getVelY() == 0)
                        getEntityRegistryInUse().addEntityObject(new Trail(gameObject.getX(), gameObject.getY(), EntityID.Trail, gameObject.getColor(), gameObject.getWidth(), gameObject.getHeight(), 0.04f));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("GameObject: " + gameObject.toString(), e);
                    }
                });

//        }
    }



    public void clearObjects() {
        // List<GameObject> gameObjects = new ArrayList<>(ClientEntityHandler.gameObjects);

        new DebugException("Cleared objects").printStackTrace();
        System.out.println("Clearing ");
        getGameObjects().clear();

//
//        ThingHandler.getGameObjects().clear();
//        ThingHandler.getGameObjectMap().clear();

    }


    public void startGame() {
        clearObjects();

        Game.setMainPlayer(new EntityPlayer((float) Game.WIDTH / 2 - 32, (float) Game.HEIGHT /2 - 32));

        Game.setScreen(null);
        Random r = UniversalHandler.RANDOM;
        int HEIGHT = Game.HEIGHT;
        int WIDTH = Game.WIDTH;

        addEntityObject(Game.getMainPlayer());
        //handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32, ID.Player,handler,hud,0));
        addEntityObject(new BasicEnemy(r.nextInt(WIDTH - 50),r.nextInt(HEIGHT - 50), EntityID.ENEMY));
        addEntityObject(new Coin(r.nextInt(WIDTH - 50), r.nextInt(Game.HEIGHT - 50), EntityID.Coin));

    }

    public void resetLevel() {
        clearObjects();

        addEntityObject(Game.getMainPlayer());
    }

    public void setPlayerInfo(EntityPlayer player) {

        if(player != null) {
            addEntityObject(player);
            Game.setMainPlayer(player);
        }
        // addObject(player);
    }

    /**
     * Individually handle every updated player
     *
     * @param entityPlayer
     */
    @Override
    protected void playerUpdate(EntityPlayer entityPlayer) {

    }

    protected <T extends GameObject> void renderEntity(Graphics g, T object) {
        IEntityRenderer<T> entityRenderer = (IEntityRenderer<T>) getRenderer(object.getClass());

        entityRenderer.render(g, object);
    }

    public void render(Graphics g) {
        for (GameObject gameObject : new ArrayList<>(getGameObjects().values())) {
            renderEntity(g, gameObject);
        }
    }
}
