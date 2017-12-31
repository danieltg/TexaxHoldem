package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.DeckOfCards.Rank;
import Engine.DeckOfCards.Suit;
import Engine.GameDescriptor.PokerBlindes;
import Engine.Players.PokerPlayer;
import Engine.Players.PlayerState;
import Engine.Players.PlayerType;
import Jaxb.Players;
import com.rundef.poker.EquityCalculator;
import com.rundef.poker.Hand;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.ArrayList;
import java.util.List;

public class PokerHand {

    private int pot;    //the amount of money in the pot
    private Card[] tableCards;
    private int maxBet;
    private int numberOfPlayers;
    private List<PokerPlayer> players;
    private Deck deck;
    private PokerBlindes blinde;
    private int currentBet;   //Current bet amount that must be called/raised
    private int dealer;      //the index of the dealer
    private int b;
    private int s;
    private int round;
    private PokerPlayer lastRaise=null;
    private PokerPlayer nextToPlay=null;
    private String lastAction;
    private int lastActionInfo;
    private int lastPlayerToPlay;
    private boolean isFinished=false;

    public PokerHand(PokerBlindes gameBlinde, List<PokerPlayer> playersInHand)
    {
        pot=0;
        currentBet=0;
        round=0;
        tableCards=new Card[5];
        deck=new Deck();

        for (int i=0; i<5; i++)
            tableCards[i]= new Card();

        lastAction="N";
        lastActionInfo=0;
        lastPlayerToPlay=-999;

        numberOfPlayers=playersInHand.size();
        players=playersInHand;
        blinde=gameBlinde;
        getStateIndex();
        updateMaxBet();
        int i=0;
        for(PokerPlayer p:players)
        {
            i++;
            if(p.getState()==PlayerState.BIG)
            {
                nextToPlay= players.get(i%this.getNumberOfPlayers());
            }
        }
    }

    public int getPot() {
        return pot;
    }

    public void updateMaxBet() {

        int maxBetByPlayers=players.get(whoIsInTheGame()).getChips();
        for (PokerPlayer p:players) {
            if (!p.isFolded() && maxBetByPlayers>p.getChips())
                maxBetByPlayers=p.getChips();
        }

        if (maxBetByPlayers<=0)
            maxBet = 0;
        else if(pot<=maxBetByPlayers)
            maxBet=pot;
        else
            maxBet=maxBetByPlayers;

    }

    public String[] getCardsAsStringArray()
    {
        String[] cards = new String[5];

        cards[0]=tableCards[0].toString();
        cards[1]=tableCards[1].toString();
        cards[2]=tableCards[2].toString();
        cards[3]=tableCards[3].toString();
        cards[4]=tableCards[4].toString();


        return cards;
    }

    public String getCardsAsString()
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
            for (PokerPlayer p:players)
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
        lastAction="CARD";
        lastActionInfo=0;
        lastPlayerToPlay=-999;
    }

    public void dealingTurnCard() {
        tableCards[3]=deck.drawCard();
        lastAction="CARD";
        lastActionInfo=0;
        lastPlayerToPlay=-999;
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

        for (PokerPlayer p:players)
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
            PokerPlayer p=getActivePlayerInIndex(index);
            String handRanking=calculator.getHandRanking(index).toString();
            Winner tmp=new Winner(p,handRanking);
            winnersList.add(tmp);

        }
        return winnersList;
    }

    private PokerPlayer getActivePlayerInIndex(int index) {

        int i=0;

        for (PokerPlayer p:players)
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
        for (PokerPlayer p:players) {
            p.setCheckOccurred(false);
            p.itIsNotMyTurn();
            p.setBet(0);
        }
    }

    public void dealingFlopCards() {

        for (int i=0; i<3; i++)
            tableCards[i]=deck.drawCard();

        lastAction="CARD";
        lastActionInfo=0;
        lastPlayerToPlay=-999;
    }


    // Deal cards to all players
    public void dealingHoleCards()
    {
        for (PokerPlayer p:players)
        {
            p.setFolded(false);
            for (int i=0; i<2; i++)
                p.setCard(deck.drawCard(),i);
        }
        lastAction="PLAYER_CARDS";
        lastActionInfo=0;
        lastPlayerToPlay=-999;
    }



    public void betSmall(){

        players.get(s).setBet(blinde.getSmall());
        pot = pot+ blinde.getSmall();
        players.get(s).collectBet();

        lastAction="B";
        lastActionInfo=blinde.getSmall();
        lastPlayerToPlay=players.get(s).getId();
    }

    public void betBig()
    {
        players.get(b).setBet(blinde.getBig());
        pot = pot +blinde.getBig();
        players.get(b).collectBet();

        lastAction="R";
        lastActionInfo=blinde.getBig();
        lastPlayerToPlay=players.get(b).getId();
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

        for (PokerPlayer p:players) {
            if (!p.isFolded())
                count++;
        }

        return count;

    }


    public List<PokerPlayer> getPlayers() {
        return players;
    }

    public int getRound() {
        return round;
    }

    public void setLastRaise(PokerPlayer lastRaisePlayer) {
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

    public PokerBlindes getBlinde() {
        return blinde;
    }

    public PokerPlayer getLastRaise() {
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
        for(PokerPlayer p:players)
        {
            if(p.isFolded()&&p.getType()==PlayerType.Human)
                return true;
        }
        return false;
    }

    public boolean isAllCheckOccurred() {
        for (PokerPlayer p:players)
        {
            if (!p.getCheckOccurred())
                return false;
        }

        return true;
    }

    public void setMaxBet(int newMaxbet)
    {
        maxBet=newMaxbet;
    }

    public Card[] getTableCards() {
        return tableCards;
    }

    public void setLastAction(String s)
    {
        lastAction=s;
    }

    public void setLastActionInfo (int i)
    {
        lastActionInfo=i;
    }
    public void setLastPlayerToPlay (int id)
    {
        lastPlayerToPlay=id;
    }

    public String getLastAction()
    {
        return lastAction;
    }

    public int getLastActionInfo()
    {
        return lastActionInfo;
    }

    public int getLastPlayerToPlay()
    {
        return lastPlayerToPlay;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public PokerPlayer getNextToPlay() {
        return nextToPlay;
    }

    public void changeNextToPlay() {
        for(PokerPlayer p:players)
            if(p.getState()==PlayerState.SMALL)
            {
                nextToPlay=p;
            }
    }
}
