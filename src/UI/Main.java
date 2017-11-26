package UI;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;


public class Main {

    public static void main(String[] args) throws InterruptedException {
        UIManager uiManager = new UIManager();
        uiManager.run();



        //shuffle cards - need to move from main


        Deck cards = new Deck();
        cards.shuffle();
        System.out.print("[");
        for (int i = 0; i < 10; i++) {
            Card c = cards.drawCard();
            System.out.print(" " + c);
        }
        System.out.println(" ]");


    }
}
