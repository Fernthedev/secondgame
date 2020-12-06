package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.universal.UniversalHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Settings {

    private String host = "127.0.0.1";
    private int port = UniversalHandler.MULTIPLAYER_PORT;

}
