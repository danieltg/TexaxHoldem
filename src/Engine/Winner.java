package Engine;

import Engine.Players.Player;

import java.io.Serializable;

public class Winner implements Serializable {

    public Player getPlayer() {
        return player;
    }

    public int getEquity() {
        return equity;
    }

    public String getHandRank() {
        return handRank;
    }

    public int getPot() {
        return pot;
    }

    private Player player;
    private int equity;
    private String handRank;
    private int pot;

    public Winner(Player player, int equity, String handRank, int pot) {
        this.player = player;
        this.equity = equity;
        this.handRank = handRank;
        this.pot=pot;
    }


}
