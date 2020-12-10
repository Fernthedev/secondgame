package io.github.fernthedev.secondgame.main.entities;

import com.github.fernthedev.universal.UniversalHandler;
import com.github.fernthedev.universal.entity.EntityPlayer;
import io.github.fernthedev.secondgame.main.Game;
import io.github.fernthedev.secondgame.main.logic.IEntityRenderer;
import io.github.fernthedev.secondgame.main.ui.api.ScreenFont;

import java.awt.*;

public class PlayerRender implements IEntityRenderer<EntityPlayer> {

    protected ScreenFont textFont = new ScreenFont(new Font("arial", Font.BOLD, 15), Color.WHITE);

    @Override
    public void render(Graphics g, EntityPlayer gameObject) {
        g.setColor(gameObject.getColor());
        g.fillRect((int)gameObject.getX(), (int)gameObject.getY(), gameObject.getWidth(), gameObject.getHeight());

        if (Game.getClient() != null && Game.getClient().isRegistered()) {
            int y = (int) (gameObject.getY() - gameObject.getHeight() / 4f);

            float maxY = (float) UniversalHandler.HEIGHT - (float) gameObject.getHeight()*2f;

            boolean outOfBoundsRoof = y - gameObject.getHeight() <= 0;
            boolean outOfBoundsFloor = y + gameObject.getHeight() >= maxY;

            if ((outOfBoundsRoof || gameObject.getVelY() > 2) && !outOfBoundsFloor)
                y = (int) (gameObject.getY() + gameObject.getHeight()*1.5);

            g.setFont(textFont.getFont());
            g.drawString(gameObject.getName(), (int) gameObject.getX(), y);
        }
    }
}
