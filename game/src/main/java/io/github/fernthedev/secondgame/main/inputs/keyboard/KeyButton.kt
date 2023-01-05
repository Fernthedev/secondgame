package io.github.fernthedev.secondgame.main.inputs.keyboard;

import lombok.*;

@RequiredArgsConstructor
@ToString
public class KeyButton {

    @Getter
    private final int charCode;

    @Getter
    @Setter
    @NonNull
    private boolean held;

    private int pressed = 0;

    public boolean isPressed() {
        if (held && pressed != 0) {
            pressed--;
            return true;
        }
        return false;
    }

}
