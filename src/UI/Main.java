package UI;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import UI.Boards.GameStateBoard;
import UI.Menus.MainMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameStateBoard board=new GameStateBoard();
        board.print();
        System.out.println("");
        MainMenu texasMainMenu=new MainMenu();
        texasMainMenu.print();
        Scanner menuScanner=new Scanner(System.in);
        System.out.flush();
        System.out.print("My choice is:");
        int choice = Integer.parseInt(menuScanner.nextLine());
        texasMainMenu.setSelectedOption(choice);

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
