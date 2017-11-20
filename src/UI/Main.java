package UI;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {

            Deck cards= new Deck();
            cards.shuffle();
            System.out.print("[");
            for (int i=0; i<10; i++)
            {
                Card c=cards.drawCard();
                System.out.print(" "+c);
            }
            System.out.println(" ]");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a file name:");
            System.out.flush();
            String filename = scanner.nextLine();

            GameDescriptor gameDescriptor= ReadGameDescriptorFile.readFile(filename);
            System.out.println(gameDescriptor.toString());

        } catch (Exception e) {
            System.out.println("Invalid Game Descriptor file: "+e.getMessage()+".");
        }
    }
}
