package Engine;

import Engine.GameDescriptor.GameDescriptor;
import Engine.Players.Player;

public class GameManager {
    private GameDescriptor gameDescriptor;
    private CurrGameState stateOfGame;

    public GameManager(GameDescriptor descriptor) {
        gameDescriptor= descriptor;
        stateOfGame=CurrGameState.NotInitialized;
    }

    public void buy(Player player, int amount)
    {
        player.buy(amount);
        //TODO: add the amount here also...
        //should it be int? or double
    }
}
