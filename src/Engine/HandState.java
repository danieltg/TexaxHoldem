package Engine;

public enum HandState {

    GameInit,
    TheFlop,
    bettingAfterFlop,
    TheTurn,
    bettingAfterTurn,
    TheRiver,
    bettingAfterRiver,
    END;

    public HandState increment() {
        int max = HandState.values().length - 1;
        return HandState.values()[HandState.inc(ordinal(), max)];
    }

    /**
     * Circularly increment i.
     */
    public static int inc(int i, int max) {
        return (i == max) ? 0 : i + 1;
    }

}


