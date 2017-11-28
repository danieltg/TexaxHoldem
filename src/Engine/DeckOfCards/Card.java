package Engine.DeckOfCards;

public class Card {

    private Rank rank;
    private Suit suit;

    public  Card()
    {
        this.rank=Rank.NA;
        this.suit=Suit.NA;
    }
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank.getRank() +""+ suit.getSuit();
    }
}
