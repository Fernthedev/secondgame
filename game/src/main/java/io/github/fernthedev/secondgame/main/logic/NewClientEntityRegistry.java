package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.GameMathUtil;
import com.github.fernthedev.INewEntityRegistry;
import com.github.fernthedev.exceptions.DebugException;
import com.github.fernthedev.game.server.NewServerEntityRegistry;
import com.github.fernthedev.packets.player_updates.SendToServerPlayerInfoPacket;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.GameObject;
import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.*;
import com.google.common.base.Stopwatch;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.entities.CoinRenderer;
import io.github.fernthedev.secondgame.main.entities.MenuParticle;
import io.github.fernthedev.secondgame.main.entities.PlayerRender;
import io.github.fernthedev.secondgame.main.entities.Trail;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NewClientEntityRegistry extends INewEntityRegistry {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Stopwatch stopwatch = Stopwatch.createUnstarted();

    public NewClientEntityRegistry() {
        DefaultEntityRenderer defaultEntityRenderer = new DefaultEntityRenderer();
        registerEntity(BasicEnemy.class, defaultEntityRenderer);
        registerEntity(FastEnemy.class, defaultEntityRenderer);
        registerEntity(SmartEnemy.class, defaultEntityRenderer);
        registerEntity(EntityPlayer.class,  new PlayerRender());
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
        if ((Game.getClient() == null && Game.getGameServer() == null) || (gameObject != null && Game.getMainPlayer() != null && gameObject.getUniqueId() == Game.getMainPlayer().getUniqueId()))
            return super.clampAndTP(gameObject);

        return null;
    }

    @Override
    public void tick() {
        super.tick();

        finishEntityUpdate();

        if (Game.getClient() != null && !stopwatch.isRunning())
            stopwatch.start();

        if (Game.getScreen() == null && Game.getClient() != null && Game.getClient().isRegistered() && Game.getMainPlayer() != null && stopwatch.elapsed(TimeUnit.MILLISECONDS) >= 500) {
            Game.getClient().sendObject(new SendToServerPlayerInfoPacket(Game.getMainPlayer(), Game.getStaticEntityRegistry().getObjectsAndHashCode()));
            stopwatch.reset();
            Game.getLogger().info("Sent update packet " + Game.getMainPlayer().toString());
        }
    }


    protected void finishEntityUpdate() {

    }



    public void clearObjects() {
        new DebugException("Cleared objects").printStackTrace();
        Game.getLogger().info("Clearing ");
        getGameObjects().clear();

    }


    public void startGame() {
        clearObjects();

        Game.setMainPlayer(new EntityPlayer((float) UniversalHandler.WIDTH / 2 - 32, (float) UniversalHandler.HEIGHT /2 - 32, ""));

        Game.setScreen(null);
        Random r = UniversalHandler.RANDOM;
        int HEIGHT = UniversalHandler.HEIGHT;
        int WIDTH = UniversalHandler.WIDTH;

        addEntityObject(Game.getMainPlayer());
        //handler.addObject(new Player(WIDTH/2-32,HEIGHT/2-32, ID.Player,handler,hud,0));
        addEntityObject(new BasicEnemy(r.nextInt(WIDTH - 50),r.nextInt(HEIGHT - 50), EntityID.ENEMY));
        addEntityObject(new Coin(r.nextInt(WIDTH - 50), r.nextInt(UniversalHandler.HEIGHT - 50), EntityID.COIN));

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



    protected <T extends GameObject> void renderEntity(Graphics g, T object) {
        IEntityRenderer<T> entityRenderer = (IEntityRenderer<T>) getRenderer(object.getClass());

        float drawX = (float) GameMathUtil.lerp(object.getPrevX(), object.getX(), Game.getElapsedTime());
        float drawY = (float) GameMathUtil.lerp(object.getPrevY(), object.getY(), Game.getElapsedTime());

        entityRenderer.render(g, object, drawX, drawY);

        if (object.isHasTrail() && !(object instanceof Trail)) {
            try {
                getEntityRegistryInUse().addEntityObject(new Trail(drawX, drawY, EntityID.TRAIL, object.getColor(), object.getWidth(), object.getHeight(), 0.04f));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("GameObject: " + object.toString(), e);
            }
        }
    }

    public void render(Graphics g) {
        for (GameObject gameObject : new ArrayList<>(getGameObjects().values())) {
            renderEntity(g, gameObject);
        }
    }

    @Override
    public Map<UUID, Integer> getObjectsAndHashCode() {
        Map<UUID, GameObject> uuidGameObjectMap = copyGameObjectsAsMap();
        return super.getObjectsAndHashCode(uuidGameObjectMap.keySet()
                .stream()
                .filter(uuid -> uuidGameObjectMap.get(uuid) != null && !(uuidGameObjectMap.get(uuid) instanceof Trail) && !(uuidGameObjectMap.get(uuid) instanceof MenuParticle))
                .collect(Collectors.toSet())
        );
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }
}
