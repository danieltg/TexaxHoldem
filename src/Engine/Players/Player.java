package Engine.Players;

import Engine.DeckOfCards.Card;
import Engine.GameManager;

import java.util.ArrayList;
import java.util.List;


public abstract class Player {

    private final int TWO=2;

    private PlayerType type;
    private PlayerState state;

    private int id;
    private String name;
    private boolean isActive;
    private int bet;

    public int getChips() {
        return chips;
    }

    private int chips;
    private int numbersOfBuy;

    private Card[] holeCards ;
    private int handsWon;

    public Player(int id){
        this.chips=0;
        this.numbersOfBuy=0;
        this.handsWon=0;
        this.state=PlayerState.NONE;
        this.id=id;
        bet=0;

        holeCards= new Card[TWO];
        for (int i=0; i<TWO; i++)
            holeCards[i]=new Card();
    };

    abstract void play();

    public void setState (PlayerState state) {this.state=state;}
    public void setType (PlayerType type)
    {
        this.type=type;
    }
    public void buy(int amount) {
        chips+=amount;
        numbersOfBuy++;
    }
    public PlayerState getState() {
        return state;
    }

    public List<String> listOfDetails() {
        List<String> list = new ArrayList<String>();
        list.add("Type: "+this.type.getType());
        list.add("State: "+this.state.getStateWithVal());
        list.add("Chips: "+this.chips);
        list.add("Buys: "+this.numbersOfBuy);
        list.add("Hands won: "+this.handsWon+"/"+ GameManager.handNumber);
        return list;
    }

    public List<String> listOfDetailesForHand()
    {
        List<String> list = new ArrayList<String>();
        list.add("Type: "+this.type.getType());
        list.add("State: "+this.state.getStateWithVal());

        if (this.bet==0)
            list.add("Bet: ??");
        else
            list.add("Bet: "+this.bet);

        list.add("Chips: "+this.chips);
        return list;
    }

    public void setCard(Card c,int index)
    {
        if (index< TWO && index>=0)
            holeCards[index]=c;

    }

    public void bet(int num)
    {
        bet=bet+num;
        chips=chips-num;
    }

    protected String getHoleCards()
    {
        return holeCards[0] + " " +holeCards[1];
    }
}
