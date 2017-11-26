package Engine.Players;

import Engine.DeckOfCards.Card;

import java.util.ArrayList;
import java.util.List;


public abstract class Player {
    private PlayerType type;
    private PlayerState state;

    private int id;
    private String name;
    private boolean isActive;

    private int chips;
    private int numbersOfBuy;

    private Card[] playerCards;
    private int handsWon;

    public Player(int id){
        this.chips=0;
        this.numbersOfBuy=0;
        this.handsWon=0;
        this.state=PlayerState.NONE;
        this.id=id;
    };

    abstract void play();

    public void setChips(int amount){this.chips+=amount;}
    public void setState (PlayerState state) {this.state=state;}
    public void setType (PlayerType type)
    {
        this.type=type;
    }
    public void buy(int amount)
    {
        chips+=amount;
        numbersOfBuy++;
    }

    public PlayerState getState() {
        return state;
    }

    public List<String> listOfDetails()
    {
        List<String> list = new ArrayList<String>();
        list.add("Type: "+this.type.getType());
        list.add("State: "+this.state.getState());
        list.add("Chips: "+this.chips);
        list.add("Buys: "+this.numbersOfBuy);
        list.add("Hands won: "+this.handsWon);

        return list;
    }
}
