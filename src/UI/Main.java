package UI;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.Players.*;
import UI.Boards.GameStateBoard;
import UI.Menus.MainMenu;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
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
