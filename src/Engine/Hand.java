package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.Blindes;
import Engine.Players.Player;
import Engine.Players.PlayerState;
import UI.Boards.GameStateBoard;

import java.util.List;

public class Hand {

    private int pot;
    private Card[] flopCards;
    private int maxBet;
    private int numberOfPlayers;
    private List<Player> players;
    private Deck deck;
    private Blindes blinde;

    private int d;
    private int b;
    private int s;
    private int n;

    public Hand(Blindes gameBlinde, List<Player> playersInHand)
    {
        pot=0;
        flopCards=new Card[5];
        deck=new Deck();

        for (int i=0; i<5; i++)
            flopCards[i]= new Card();

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
            s=s+ flopCards[i].toString()+  " | ";

        s=s+flopCards[4].toString();

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
        betSmallAndBig();
        dealingHoleCards();
        firstBettingRound();
        dealingFlopCards();

        GameStateBoard.printHandState(players,printHand());


    }

    private void dealingFlopCards() {

        for (int i=0; i<3; i++)
            flopCards[i]=deck.drawCard();

    }


    private void dealingHoleCards()
    {
        for (Player p:players)
        {
            for (int i=0; i<2; i++)
                p.setCard(deck.drawCard(),i);
        }
    }



    private void betSmallAndBig(){
        players.get(s).bet(blinde.getSmall());
        pot+=blinde.getSmall();

        players.get(b).bet(blinde.getBig());
        pot+=blinde.getBig();
    }
    private void firstBettingRound() {



    }

    private void getStateIndex()
    {
        for (int i=0; i<numberOfPlayers; i++)
        {
            if (players.get(i).getState()== PlayerState.DEALER)
                d= i;
            else if (players.get(i).getState()== PlayerState.SMALL)
                s=i;
            else if (players.get(i).getState()== PlayerState.BIG)
                b=i;
            else
                n=i;

        }
    }


}
