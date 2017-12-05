package Engine.Players;

import Engine.GameManager;

public enum PlayerState {

    DEALER("D"),
    SMALL("S"),
    BIG("B"),
    NONE("");

    private String state;

    PlayerState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }


}
