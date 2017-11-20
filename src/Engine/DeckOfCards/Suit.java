package Engine.DeckOfCards;

public enum Suit {
    CLUB('C'),
    DIAMOND('D'),
    HEART('H'),
    SPADE('S');

    private char suitChar;

    Suit(char suit) {
        this.suitChar = suit;
    }

    public char getSuitChar() {
        return this.suitChar;
    }

}