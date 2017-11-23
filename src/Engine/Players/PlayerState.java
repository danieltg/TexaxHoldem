package Engine.Players;

public enum PlayerState {

    DEALER("D"),
    SMALL("S"),
    BIG("B"),
    NONE("");

    private String stateChar;

    PlayerState(String state) {
        this.stateChar = state;
    }

    public String getState() {
        return this.stateChar;
    }
}
