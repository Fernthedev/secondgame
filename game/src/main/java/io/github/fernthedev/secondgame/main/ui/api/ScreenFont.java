package io.github.fernthedev.secondgame.main.ui.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import java.awt.*;

@Data
@AllArgsConstructor
@Getter
public class ScreenFont {

    @NonNull
    private Font font;

    @NonNull
    private Color color;

    public int getSize() {
        return font.getSize();
    }
}
