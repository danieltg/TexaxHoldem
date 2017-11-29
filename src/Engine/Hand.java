package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.Blindes;
import Engine.Players.Player;
import Engine.Players.PlayerState;
import Engine.Players.PlayerType;
import UI.Boards.GameStateBoard;
import UI.Menus.HandMenu;

import java.util.List;

public class Hand {

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

    public Hand(Blindes gameBlinde, List<Player> playersInHand)
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
    
    
    public void play()
    {
        dealingHoleCards();
        collectBets();

        if (playersLeft() == 1)
            evaluateRound();

        GameStateBoard.printHandState(players,printHand());
        dealingFlopCards();
        collectBets();

        if (playersLeft() == 1)
            evaluateRound();

        GameStateBoard.printHandState(players,printHand());
        dealingTurnCard();
        collectBets();

        if (playersLeft() == 1)
            evaluateRound();

        GameStateBoard.printHandState(players,printHand());
        dealingRiverCard();
        collectBets();
        evaluateRound();

        getUserSelection();
        GameStateBoard.printHandState(players,printHand());

    }

    private void dealingRiverCard() {
        tableCards[4]=deck.drawCard();
    }

    private void dealingTurnCard() {
        tableCards[3]=deck.drawCard();
    }


    private void evaluateRound() {
    }

    // Collect all bets
    private void collectBets() {

        int p;
        boolean checkOccurred = false;

        if (round==0)
        {
            betSmallAndBig();
            p=2;
            currentBet=blinde.getBig();
        }
        else
        {
            p=1;
            currentBet = 0;
        }

        lastRaise = players.get((p + dealer) % numberOfPlayers);
        int currIndex;

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
        }

        resetPlayersBets();
        round++;
    }

    private void doThis(String whatToDo, Player p) {

        while (true) {

            if (whatToDo== "F")
            {
                p.setFolded(true);
                break;
            }

            if (whatToDo=="C")
            {
                p.addChips(p.getBet());
                pot -= p.getBet();

                if (p.getChips() < currentBet)
                    p.setBet(p.getChips());
                else
                    p.setBet(currentBet);

                break;

            }

            if (whatToDo=="R")
            {
                pot -= p.getBet();
                p.addChips(p.getBet());

                System.out.print("What would you like to raise to? ");
                int raiseTo = 0;

                while (raiseTo == 0) {
                    try {
                        raiseTo = Integer.parseInt(System.console().readLine());
                    } catch (NumberFormatException e) {
                        System.out.print("Please enter a valid bet: ");
                        raiseTo = 0;
                        continue;
                    }
                    if (raiseTo <= currentBet || raiseTo > p.getChips() || raiseTo>maxBet) {
                        System.out.print("Please enter a valid bet: ");
                        raiseTo = 0;
                    }
                }

                currentBet=raiseTo;
                lastRaise = p;
                p.setBet(currentBet);
                break;


            }

            if (whatToDo=="K")
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
        pot+=blinde.getSmall();

        players.get(b).setBet(blinde.getBig());
        pot+=blinde.getBig();
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
            if (p.isFolded())
                count++;
        }

        return count;

    }

}
