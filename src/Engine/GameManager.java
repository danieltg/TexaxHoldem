package Engine;

import Engine.GameDescriptor.GameDescriptor;
import Engine.Players.Player;

public class GameManager {
    private GameDescriptor gameDescriptor;
    private CurrGameState stateOfGame;

    public  GameManager(){
        stateOfGame=CurrGameState.NotInitialized;
    };

    public void setGameDescriptor(GameDescriptor gameDescriptor) {
        this.gameDescriptor = gameDescriptor;
        this.stateOfGame=CurrGameState.Initialized;
    }


    public CurrGameState GetStateOfGame()
    {
        return stateOfGame;
    }
    public void buy(Player player, int amount)
    {
        player.buy(amount);
        //TODO: add the amount here also...
        //should it be int? or double
    }

    public GameDescriptor GetGameDescriptor() {
        return gameDescriptor;
    }
}
