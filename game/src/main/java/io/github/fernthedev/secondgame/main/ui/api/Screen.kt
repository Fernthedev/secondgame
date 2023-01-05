package io.github.fernthedev.secondgame.main.ui.api;

import com.github.fernthedev.lightchat.core.StaticHandler;
import com.github.fernthedev.universal.EntityID;
import com.github.fernthedev.universal.UniversalHandler;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.entities.MenuParticle;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Screen {

    private final String title;

    @Nullable
    @Getter
    @Setter
    protected Screen parentScreen = null;

    protected final ScreenButton DEFAULT_BACK_BUTTON = new ScreenButton("Back", this::returnToParentScreen);

    @Getter
    protected int buttonSpacing = 10;

    @Getter
    protected boolean setParentScreenOnSet = true;

    @Getter
    private int yButtonIncrement = resetY();

    public int incrementY(int increment) {
        return yButtonIncrement += increment + buttonSpacing;
    }

    public int resetY() {
        return yButtonIncrement = buttonYStart() - buttonSpacing;
    }

    public Screen(String title) {
        this.title = title;
    }

    @Getter
    protected ScreenFont textFont = new ScreenFont(new Font("arial", Font.BOLD, 50), Color.WHITE);

    @Getter
    protected List<ScreenButton> buttonList = new ArrayList<>();


    public void addButton(ScreenButton button) {
        buttonList.add(button);
    }

    protected abstract void draw(Graphics g);

    private static final int buttonYStart = (int) (UniversalHandler.HEIGHT - (UniversalHandler.HEIGHT * 0.7));
    private static final int xCenter = UniversalHandler.WIDTH / 2;

    public int buttonX(ScreenButton screenButton) {
        return xCenter - (screenButton.getButtonSize().getWidth() / 2);
    }

    public int buttonYStart() {
        return buttonYStart;
    }

    public void render(Graphics g) {
        buttonList.clear();
        resetY();
        draw(g);

        g.setFont(textFont.getFont());
        g.setColor(textFont.getColor());

        g.drawString(title, UniversalHandler.WIDTH / 2 - (textFont.getSize() / 2 * title.length()), (int) (UniversalHandler.HEIGHT - (UniversalHandler.HEIGHT * 0.85)));

        if (StaticHandler.isDebug())
            g.drawRect(UniversalHandler.WIDTH / 2 - (textFont.getSize() / 2 * title.length()), (int) (UniversalHandler.HEIGHT - (UniversalHandler.HEIGHT - (UniversalHandler.HEIGHT * 0.2))), 15, 15);

        for (ScreenButton button : buttonList) {
            button.parentScreen = this;

            int x = buttonX(button);
            int y = getYButtonIncrement();

            if (button.render(g, new Location(x, y))) {
                incrementY(button.getButtonSize().getHeight());

            }
        }
    }

    public void returnToParentScreen() {
        Game.setScreen(parentScreen);
    }

    protected static void addMenuParticles() {
        Random r = UniversalHandler.RANDOM;

        Game.getStaticEntityRegistry().clearObjects();


        int WIDTH = UniversalHandler.WIDTH;
        int HEIGHT = UniversalHandler.HEIGHT;

        int amount = r.nextInt(15);
        if (amount < 10) amount = 10;
        for (int i = 0; i < amount; i++) {
            Game.getStaticEntityRegistry().addEntityObject(new MenuParticle(r.nextInt(WIDTH - 40) + 20, r.nextInt(HEIGHT - 40) + 20, EntityID.MENU_PARTICLE));
        }
    }




}
