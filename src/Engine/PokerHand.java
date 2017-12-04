package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.Blindes;
import Engine.Players.Player;
import Engine.Players.PlayerState;
import Engine.Players.PlayerType;
import UI.Boards.GameStateBoard;
import UI.Menus.HandMenu;
import com.rundef.poker.EquityCalculator;
import com.rundef.poker.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PokerHand {

    private int pot;    //the amount of money in the pot
    private Card[] tableCards;
    private int maxBet;
    private int numberOfPlayers;
    private List<Player> players;
    private Deck deck;
    private Blindes blinde;
    private int currentBet;   //Current bet amount that must be called/raised
    private int dealer;      //the index of the dealer
    private int b;
    private int s;
    private int n;
    private int round;
    private Player lastRaise;

    public PokerHand(Blindes gameBlinde, List<Player> playersInHand)
    {
        pot=0;
        currentBet=0;
        round=0;

        tableCards=new Card[5];
        deck=new Deck();

        for (int i=0; i<5; i++)
            tableCards[i]= new Card();

        numberOfPlayers=playersInHand.size();
        players=playersInHand;
        blinde=gameBlinde;
        getStateIndex();
        updateMaxBet();
    }

    private void updateMaxBet() {
        maxBet=players.get(0).getChips();

        for (int i=1; i<numberOfPlayers; i++)
        {
            if (maxBet<players.get(i).getChips())
                maxBet=players.get(i).getChips();
        }

    }


    public String getCardsAsString ()
    {
        String s="";

        for (int i=0; i<4; i++)
            s=s+ tableCards[i].toString()+  " | ";

        s=s+tableCards[4].toString();

        return s;

    }
    public String printHand()
    {
        String handStr=getCardsAsString()+
                "  POT: "+pot;

        return handStr;
    }

    public List<Winner> getWinner() {

        List<Winner> winnersList = new ArrayList<Winner>();
        int index=whoIsInTheGame();
        String cards=players.get(index).getHoleCards();
        Winner winner=new Winner(players.get(index),100,cards,pot);
        winnersList.add(winner);
        return winnersList;
    }


    private int whoIsInTheGame()
    {
     for (int i=0; i<numberOfPlayers; i++)
         if (!players.get(i).isFolded())
             return i;

     return 0;
    }

    private void dealingRiverCard() {
        tableCards[4]=deck.drawCard();
    }

    public void dealingTurnCard() {
        tableCards[3]=deck.drawCard();
    }


    public List<Winner> evaluateRound() throws Exception {

        int numOfPlayerHands=0;

        EquityCalculator calculator = new EquityCalculator();
        calculator.reset();
        String tableCardsStr="";

        for (Card c:tableCards)
            tableCardsStr=tableCardsStr+c.toString();

        calculator.setBoardFromString(tableCardsStr);

        for (Player p:players)
        {
            if (!p.isFolded())
            {
                String playerCards=(p.getHoleCards()).replaceAll("\\s+","");
                Hand hand = Hand.fromString(playerCards);
                calculator.addHand(hand);
                numOfPlayerHands++;
            }
        }
        calculator.calculate();

        return getWinners(calculator,numOfPlayerHands);
    }

    private List<Winner> getWinners(EquityCalculator calculator, int numOfPlayerHands)
    {

        List<Winner> winnersList = new ArrayList<Winner>();
        int index=0;

        for (Player p:players)
        {
            if (!p.isFolded() && index<numOfPlayerHands)
            {
                int equity=calculator.getHandEquity(index).getEquity();
                if (equity>0)
                {
                    String handRanking=calculator.getHandRanking(index).toString();
                    Winner tmp=new Winner(p,equity,handRanking,this.pot);
                    winnersList.add(tmp);
                }
                index++;
            }
        }

        return winnersList;
    }

    public void resetPlayersBets()
    {
        for (Player p:players)
            p.setBet(0);
    }

    public void dealingFlopCards() {

        for (int i=0; i<3; i++)
            tableCards[i]=deck.drawCard();

    }


    // Deal cards to all players
    public void dealingHoleCards()
    {
        for (Player p:players)
        {
            p.setFolded(false);
            for (int i=0; i<2; i++)
                p.setCard(deck.drawCard(),i);
        }
    }



    public void betSmallAndBig(){
        players.get(s).setBet(blinde.getSmall());
        players.get(b).setBet(blinde.getBig());

        pot = blinde.getSmall() +blinde.getBig();
        players.get(s).collectBet();
        players.get(b).collectBet();

        players.get(s).setBet(0);
        players.get(b).setBet(0);
    }

    private void getStateIndex()
    {
        for (int i=0; i<numberOfPlayers; i++)
        {
            if (players.get(i).getState()== PlayerState.DEALER)
                dealer= i;
            else if (players.get(i).getState()== PlayerState.SMALL)
                s=i;
            else if (players.get(i).getState()== PlayerState.BIG)
                b=i;
            else
                n=i;

        }
    }

    public int playersLeft()
    {
        int count=0;

        for (Player p:players) {
            if (!p.isFolded())
                count++;
        }

        return count;

    }


    public List<Player> getPlayers() {
        return players;
    }

    public int getRound() {
        return round;
    }

    public void setLastRaise(Player lastRaisePlayer) {
        lastRaise=lastRaisePlayer;
    }

    public int getDealer() {
        return dealer;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setCurrentBet(int currbet) {
        currentBet=currbet;
    }

    public Blindes getBlinde() {
        return blinde;
    }

    public Player getLastRaise() {
        return lastRaise;
    }

    public void addToPot(int bet) {
        pot+=bet;
    }

    public void upRound() {
        round++;
    }

    public void subFromPot(int bet) {
        pot=pot-bet;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getMaxBet() {
        return maxBet;
    }
}
