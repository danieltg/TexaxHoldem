package Engine;

import Engine.Players.Player;

import java.io.Serializable;

public class Winner implements Serializable {

    private Player player;
    private String handRank;

    public Winner(Player player, String handRank) {
        this.player = player;
        this.handRank = handRank;
    }
    public String getHandRank() {
        return handRank;
    }
    public Player getPlayer() {
        return player;
    }

}
