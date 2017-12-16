package Engine;

import Engine.DeckOfCards.Card;
import Engine.Players.PokerPlayer;

import java.io.Serializable;
import java.util.List;

public class PokerHandStep implements Serializable {

    private List<PokerPlayer> players;
    private int playerIndex;
    private int pot;
    private Card[] tableCards;
    private int currentBet;   //Current bet amount that must be called/raised
    private String action;
    private int additionActionInformation;

    public PokerHandStep(List<PokerPlayer> players, int playerIndex, int pot, Card[] tableCards, int currentBet, String action, int additionActionInformation) {
        this.players = players;
        this.playerIndex = playerIndex;
        this.pot = pot;
        this.tableCards = tableCards;
        this.currentBet = currentBet;
        this.action = action;
        this.additionActionInformation = additionActionInformation;
    }
}
