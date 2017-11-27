package Engine.Players;

import Engine.GameManager;

public enum PlayerState {

    DEALER("D"),
    SMALL("S"),
    BIG("B"),
    NONE("");

    private String state;
    private int value;

    PlayerState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }

    public String getStateWithVal()
    {
        switch (state)
        {
            case "S":
                return this.state+ " ("+GameManager.getSmall()+")";
            case "B":
                return this.state+ " ("+GameManager.getBig()+")";
            default:
                return this.state;
        }
    }
}
