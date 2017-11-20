package Engine.DeckOfCards;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {
    private List<Card> cards;

    public Deck()
    {
        this.cards=new LinkedList<>();
        for (Suit s:Suit.values())
        {
            for (Rank r:Rank.values())
            {
                this.cards.add(new Card(r,s));
            }
        }

    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public Card drawCard() throws NoSuchElementException {
        if (this.cards.isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.cards.remove(0);
    }


}
