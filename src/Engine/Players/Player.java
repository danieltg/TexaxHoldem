package Engine.Players;

import Engine.DeckOfCards.Card;

public abstract class Player {
    private PlayerType type;
    private PlayerState state;

    private int id;
    private String name;
    private boolean isActive;

    private int chips=0;
    private int numbersOfBuy=0;

    private Card[] playerCards;

    private int handsWon=0;

    public Player(){};

    abstract void play();

    public void buy(int amount)
    {
        chips+=amount;
        numbersOfBuy++;
    }

}
