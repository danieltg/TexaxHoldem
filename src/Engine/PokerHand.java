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
    
    
    public List<Winner> play() throws Exception {

        dealingHoleCards();
        betSmallAndBig();
        collectBets();

        if (playersLeft() == 1)
            return getWinner();

        GameStateBoard.printHandState(players,printHand());
        dealingFlopCards();
        collectBets();

        if (playersLeft() == 1)
            return getWinner();

        //GameStateBoard.printHandState(players,printHand());
        dealingTurnCard();
        collectBets();

        if (playersLeft() == 1)
            return getWinner();

        dealingRiverCard();
        collectBets();
        //GameStateBoard.printHandState(players,printHand());

        return evaluateRound();
    }

    private List<Winner> getWinner() {

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

    private void dealingTurnCard() {
        tableCards[3]=deck.drawCard();
    }


    private List<Winner> evaluateRound() throws Exception {

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

    // Collect all bets
    private void collectBets() {

        int p;
        int currIndex;
        boolean checkOccurred = false;

        if (round==0) {
            lastRaise = players.get((2 + dealer) % numberOfPlayers);
            p = 3;
            currentBet = blinde.getBig();
        }
        else {
            p = 1;
            lastRaise = players.get((p + dealer) % numberOfPlayers);
            currentBet = 0;
        }

        while (true) {
            if (playersLeft() == 1)
                break;

            currIndex = (p + dealer) % numberOfPlayers;
            Player currPlayer = players.get(currIndex);

            if (lastRaise != currPlayer || !checkOccurred) {
                if (!currPlayer.isFolded() && currPlayer.getChips() > 0) {
                    if(currPlayer.getType()== PlayerType.Human)
                        GameStateBoard.printHandState(players,printHand());

                    doThis(currPlayer.play(),currPlayer);

                    pot += currPlayer.getBet();
                    currPlayer.collectBet();
                } else
                    break;
            }

            checkOccurred = true;
            p++;
            round++;
        }

        resetPlayersBets();
    }


    private void doThis(String whatToDo, Player p) {

        while (true) {

            if (whatToDo.equals("F"))
            {
                p.setFolded(true);
                break;
            }

            if (whatToDo.equals("C"))
            {
                p.addChips(p.getBet());
                pot -= p.getBet();

                if (p.getChips() < currentBet)
                    p.setBet(p.getChips());
                else
                    p.setBet(currentBet);

                break;
            }

            if (whatToDo.equals("R"))
            {
                pot -= p.getBet();
                p.addChips(p.getBet());

                int raiseTo = 0;
                while (raiseTo == 0) {
                    try {
                        //TODO:I'm not sure the min and mac are correct
                        raiseTo = p.getRaise(currentBet, maxBet);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid bet: ");
                        raiseTo = 0;
                        continue;
                    }
                    if (raiseTo <= currentBet || raiseTo > p.getChips() || raiseTo>maxBet) {
                        System.out.println("Please enter a valid bet: ");
                        raiseTo = 0;
                    }
                }

                currentBet=raiseTo;
                lastRaise = p;
                p.setBet(currentBet);
                break;
            }

            if (whatToDo.equals("K"))
            {
                break;
            }

        }
    }

    private void resetPlayersBets()
    {
        for (Player p:players)
            p.setBet(0);
    }

    public static String getUserSelection()
    {
        boolean validSelection=false;
        String selection="";

        while (!validSelection)
        {
            HandMenu.print();
            try {
                selection= HandMenu.getOptionFromUser();
                System.out.println("Valid selection, your selection is: "+selection);
                validSelection=true;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

        }

        return selection;

    }

    private void dealingFlopCards() {

        for (int i=0; i<3; i++)
            tableCards[i]=deck.drawCard();

    }


    // Deal cards to all players
    private void dealingHoleCards()
    {
        for (Player p:players)
        {
            p.setFolded(false);
            for (int i=0; i<2; i++)
                p.setCard(deck.drawCard(),i);
        }
    }



    private void betSmallAndBig(){
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

    private int playersLeft()
    {
        int count=0;

        for (Player p:players) {
            if (!p.isFolded())
                count++;
        }

        return count;

    }

}
