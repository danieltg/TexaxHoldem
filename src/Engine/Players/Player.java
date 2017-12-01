package Engine.Players;

import Engine.DeckOfCards.Card;
import Engine.GameManager;

import java.util.ArrayList;
import java.util.List;


public abstract class Player {

    private PlayerType type;
    private PlayerState state;

    private int id;
    private String name;
    private boolean folded;  //whether or not a player has folded
    private int bet;        //The amount that a player is betting in one session of betting


    private int chips;      //The amount of money that a player has
    private int numbersOfBuy;

    private Card[] holeCards ;
    private int handsWon;

    public Player(int playerID){

        chips=0;
        bet =0;
        folded=false;

        numbersOfBuy=0;
        handsWon=0;
        state=PlayerState.NONE;
        id=playerID;

        holeCards= new Card[2];
        for (int i=0; i<2; i++)
            holeCards[i]=new Card();
    };

    public int getChips() {
        return chips;
    }

    abstract public String play();

    public void setState (PlayerState state) {this.state=state;}
    public void setType (PlayerType type)
    {
        this.type=type;
    }
    public void buy(int amount) {
        addChips(amount);
        numbersOfBuy++;
    }

    public void addChips (int amount)
    {
        chips=chips+amount;
    }

    public PlayerState getState() {
        return state;
    }

    public List<String> listOfDetails() {
        List<String> list = new ArrayList<String>();
        list.add("Type: "+this.type.getType() +" ("+id+")");
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
        if (index< 2 && index>=0)
            holeCards[index]=c;

    }

    public void collectBet()
    {
        chips=chips-bet;
    }

    public int getBet() {
        return bet;
    }

    protected String getHoleCards()
    {
        return holeCards[0] + " " +holeCards[1];
    }

    public void setBet(int newBet)
    {
        // Checks to make sure the bet is valid
        if (newBet < 0) {
            System.out.println("Invalid bet detected");
        } else if (newBet > chips) {
            bet = chips;
        } else {
            bet = newBet;
        }

    }


    public void setFolded(boolean status)
    {
        folded=status;
    }

    public boolean isFolded() {
        return folded;
    }

    public PlayerType getType() {
        return type;
    }

    public void isAWinner()
    {
        handsWon++;
    }
}
