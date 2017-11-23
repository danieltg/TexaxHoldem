package Engine;

import Engine.GameDescriptor.GameDescriptor;

public class GameManager {
    private GameDescriptor gameDescriptor;

    public void buy(Player player,int amount)
    {
        player.buy(amount);
        //TODO: add the amount here also...
        //should it be int? or double
    }
}
