package io.github.fernthedev.secondgame.main.ui.api;

import com.github.fernthedev.lightchat.core.StaticHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.awt.*;

public class ScreenButton {


    private String string;


    protected Screen parentScreen;

    @Getter
    private final Runnable onClick;

    protected int defaultButtonWidth = 200, defaultButtonHeight = 64, defaultButtonWidthSpace = 30;

    @NonNull
    protected ScreenFont buttonFont = new ScreenFont(new Font("arial", Font.BOLD, 30), Color.WHITE );

    @Setter
    protected Size buttonSize = null;

    @Getter
    @Setter
    @Nullable
    protected Location location = null;

    @Getter
    protected Location renderedLocation;

    public ScreenButton(String string, Runnable onClick) {
        this.string = string;
        this.onClick = onClick;
    }

    public ScreenButton(String string, Runnable onClick, Size buttonSize) {
        this.string = string;
        this.onClick = onClick;
        this.buttonSize = buttonSize;
    }

    public ScreenButton(String string, Runnable onClick, Size buttonSize, Location location) {
        this.string = string;
        this.onClick = onClick;
        this.buttonSize = buttonSize;
        this.location = location;
    }

    public ScreenButton(String string, Runnable onClick, Location location) {
        this.string = string;
        this.onClick = onClick;
        this.location = location;
    }

    public ScreenButton(String string, Runnable onClick, @NonNull ScreenFont buttonFont, Size buttonSize, Location location) {
        this.string = string;
        this.onClick = onClick;
        this.buttonFont = buttonFont;
        this.buttonSize = buttonSize;

        this.location = location;
    }

    /**
     *
     * @param g
     * @param loc default location
     * @return true if using the location defined in parameter
     */
    public boolean render(Graphics g, Location loc) {
        getButtonSize();
        boolean returnStatus = false;

        Location buttonDrawLocation = this.location;

        if (buttonDrawLocation == null) {
            Validate.notNull(loc);
            buttonDrawLocation = loc;

            returnStatus = true;
        } else {
            if (buttonDrawLocation.getX() == null) {
                Validate.notNull(loc);
                buttonDrawLocation.setX(loc.getX());
            }

            if (buttonDrawLocation.getY() == null) {
                Validate.notNull(loc);
                buttonDrawLocation.setX(loc.getY());
            }
        }

        renderedLocation = buttonDrawLocation;
                        // 70
        int stringX = (int) (buttonDrawLocation.getX() + (buttonSize.getWidth()/2.0) - (buttonFont.getSize() * string.length() * 0.26)); //((((string.length() / buttonFont.getSize())) + (buttonSize.getWidth())) + buttonSize.getWidth()/2); //- ((buttonSize.getWidth() / 2) ) ; //( (width / 2) - (size + (string.length() * 2) - string.length() / 2));

        int stringY = buttonDrawLocation.getY() + ( buttonSize.getHeight() - (buttonFont.getSize() - buttonSize.getHeight()/8));



        g.setFont(buttonFont.getFont());
        g.setColor(buttonFont.getColor());

        g.drawRect(buttonDrawLocation.getX(), buttonDrawLocation.getY(), buttonSize.getWidth(), buttonSize.getHeight());
        g.drawString(string, stringX, stringY);

        if (StaticHandler.isDebug())
            g.drawRect(stringX, stringY, 15, 15);

        return returnStatus;
    }

    protected Size getSizeDependingLength() {
        int width = defaultButtonWidth;

        if (string.length()*(buttonFont.getSize()/2) > (width - defaultButtonWidthSpace*2) / 2) {
            width = ((string.length()*buttonFont.getSize()/2) + width/2); //(string.length() + defaultButtonWidthSpace + (width/2)) * 2;
        }

        return new Size(width, defaultButtonHeight);
    }


    public Size getButtonSize() {
        if (buttonSize == null)
            buttonSize = getSizeDependingLength();

        return buttonSize;
    }
}
