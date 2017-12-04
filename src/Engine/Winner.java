package Engine;

import Engine.Players.Player;

import java.io.Serializable;

public class Winner implements Serializable {

    public Player getPlayer() {
        return player;
    }

    public String getHandRank() {
        return handRank;
    }

    public int getPot() {
        return pot;
    }

    private Player player;
    private String handRank;
    private int pot;

    public Winner(Player player, String handRank, int pot) {
        this.player = player;
        this.handRank = handRank;
        this.pot=pot;
    }


}
