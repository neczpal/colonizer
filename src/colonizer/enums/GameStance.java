package colonizer.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GameStance {
    PREPARING,

    PICK_1ST_VILLAGE("pick"),
    PICK_2ND_VILLAGE("pick"),
    PRE_ROLL("play", "roll"),
    ROBBER_MOVE ("rob"),
    CHOSE_ROBBER_TARGET("target"),
    POST_ROLL("build", "buy", "play", "trade", "end");

    private List<String> availableCommands = new ArrayList<> ();

    GameStance(String... availableCommands) {
        this.availableCommands.addAll (Arrays.asList (availableCommands));
    }

    public boolean isAvailable(String command) {
        return availableCommands.contains (command);
    }

}
