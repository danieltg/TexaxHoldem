package Engine;

import Engine.Players.Player;

import java.io.Serializable;

public class Winner implements Serializable {

    public Player getPlayer() {
        return player;
    }

    private Player player;



    private String handRank;

    public Winner(Player player, String handRank) {
        this.player = player;
        this.handRank = handRank;
    }
    public String getHandRank() {
        return handRank;
    }

}
