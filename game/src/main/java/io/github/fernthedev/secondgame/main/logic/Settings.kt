package io.github.fernthedev.secondgame.main.logic;

import com.github.fernthedev.lightchat.client.ClientSettings;
import com.github.fernthedev.universal.UniversalHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Settings extends ClientSettings {

    private String name = "";
    private String host = "127.0.0.1";
    private int port = UniversalHandler.MULTIPLAYER_PORT;

}
