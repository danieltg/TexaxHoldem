package Engine;

import Engine.DeckOfCards.Card;

import java.io.Serializable;
import java.util.List;

public class HandReplay implements Serializable {

    private Card[] tableCards;
    private List<PokerHandStep> steps;
    private int numberOfSteps;

    public HandReplay(Card[] tableCards, List<PokerHandStep> steps) {
        this.tableCards = tableCards;
        this.steps = steps;
        this.numberOfSteps = steps.size();
    }

    public Card[] getTableCards() {
        return tableCards;
    }

    public List<PokerHandStep> getSteps() {
        return steps;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }


}
