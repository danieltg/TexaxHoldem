package Engine.DeckOfCards;

public enum Rank {
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    private char rankChar;

    Rank(char rank) {
        this.rankChar = rank;
    }

    public char getRankChar() {
        return this.rankChar;
    }

}