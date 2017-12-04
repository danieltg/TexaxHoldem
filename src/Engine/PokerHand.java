package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.DeckOfCards.Rank;
import Engine.DeckOfCards.Suit;
import Engine.GameDescriptor.Blindes;
import Engine.Players.Player;
import Engine.Players.PlayerState;
import Engine.Players.PlayerType;
import com.rundef.poker.EquityCalculator;
import com.rundef.poker.Hand;

import java.util.ArrayList;
import java.util.List;

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
    private int round;
    private Player lastRaise=null;

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

    public int getPot() {
        return pot;
    }

    public void updateMaxBet() {
        int maxBetByPlayers=players.get(0).getChips();
//add min pot
        for (int i=1; i<numberOfPlayers; i++)
        {
            if (maxBetByPlayers<players.get(i).getChips())
                maxBetByPlayers=players.get(i).getChips();
        }
        if(pot<=maxBetByPlayers)
            maxBet=pot;
        else
            maxBet=maxBetByPlayers;
    }


    private String getCardsAsString()
    {
        StringBuilder s= new StringBuilder();

        for (int i=0; i<4; i++)
            s.append(tableCards[i].toString()).append(" | ");

        s.append(tableCards[4].toString());

        return s.toString();

    }
    public String printHand()
    {

        return getCardsAsString()+
                "  POT: "+pot;
    }

    public List<Winner> getWinner() {

        List<Winner> winnersList = new ArrayList<>();

        if (playersLeft() == 1) {
            int index = whoIsInTheGame();
            String cards = players.get(index).getHoleCards();
            Winner winner = new Winner(players.get(index), cards);
            winnersList.add(winner);
        }

        //Human player left the game
        else
        {
            for (Player p:players)
            {
                if (!p.isFolded()) {
                    String cards = p.getHoleCards();
                    Winner winner = new Winner(p, cards);
                    winnersList.add(winner);
                }
            }
        }

        return winnersList;
    }


    private int whoIsInTheGame()
    {
     for (int i=0; i<numberOfPlayers; i++)
         if (!players.get(i).isFolded())
             return i;

     return 0;
    }

    public void dealingRiverCard() {
        tableCards[4]=deck.drawCard();
    }

    public void dealingTurnCard() {
        tableCards[3]=deck.drawCard();
    }


    public List<Winner> evaluateRound() throws Exception {

        EquityCalculator calculator = new EquityCalculator();
        calculator.reset();
        StringBuilder tableCardsStr= new StringBuilder();

        for (Card c:tableCards) {
            if (c.getSuit()!= Suit.NA && c.getRank()!= Rank.NA)
                tableCardsStr.append(c.toString());
        }

        calculator.setBoardFromString(tableCardsStr.toString());

        for (Player p:players)
        {
            if (!p.isFolded())
            {
                String playerCards=(p.getHoleCards()).replaceAll("\\s+","");
                Hand hand = Hand.fromString(playerCards);
                calculator.addHand(hand);
            }
        }
        calculator.calculate();
        return getWinners(calculator);
    }

    private List<Winner> getWinners(EquityCalculator calculator)
    {

        List<Winner> winnersList = new ArrayList<>();
        List<Integer> winningHands= calculator.getWinningHands();
        for (int index: winningHands)
        {
            Player p=getActivePlayerInIndex(index);
            String handRanking=calculator.getHandRanking(index).toString();
            Winner tmp=new Winner(p,handRanking);
            winnersList.add(tmp);

        }

        return winnersList;
    }

    private Player getActivePlayerInIndex(int index) {

        int i=0;

        for (Player p:players)
        {
            if (!p.isFolded())
            {
                if (i==index)
                    return p;
                else
                    i++;
            }
        }

        return null;
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

    public boolean humanIsLeft() {
        for(Player p:players)
        {
            if(p.isFolded()&&p.getType()==PlayerType.Human)
                return true;
        }
        return false;
    }
}
