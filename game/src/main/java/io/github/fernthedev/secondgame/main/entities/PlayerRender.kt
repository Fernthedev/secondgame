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
    public void render(Graphics g, EntityPlayer gameObject, float drawX, float drawY) {
//        float drawX = gameObject.getX() + (gameObject.getX() - gameObject.getPrevX()) * Game.getRenderTime();
//        float drawY = gameObject.getY() + (gameObject.getY() - gameObject.getPrevY()) * Game.getRenderTime();


        g.setColor(gameObject.getColor());
        g.fillRect((int)drawX, (int) drawY, gameObject.getWidth(), gameObject.getHeight());

        if (Game.getClient() != null && Game.getClient().isRegistered()) {
            int y = (int) (drawY - gameObject.getHeight() / 4f);

            float maxY = (float) UniversalHandler.HEIGHT - (float) gameObject.getHeight()*2f;

            boolean outOfBoundsRoof = y - gameObject.getHeight() <= 0;
            boolean outOfBoundsFloor = y + gameObject.getHeight() >= maxY;

            if ((outOfBoundsRoof || gameObject.getVelY() > 2) && !outOfBoundsFloor)
                y = (int) (drawY + gameObject.getHeight()*1.5);

            g.setFont(textFont.getFont());
            g.drawString(gameObject.getName(), (int) gameObject.getX(), y);
        }
    }
}
