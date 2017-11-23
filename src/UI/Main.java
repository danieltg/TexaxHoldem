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

        MainMenu mainMenu=new MainMenu();
        mainMenu.Print();
        //check of GameState
        List<Player> players = new ArrayList<Player>();
        players.add(new HumanPlayer());
        players.get(0).setChips(100);
        players.add(new ComputerPlayer());
        players.get(1).setChips(600);
        players.add(new HumanPlayer());
        players.get(2).setChips(400);
        players.add(new ComputerPlayer());
        players.get(3).setChips(200);
        GameStateBoard board=new GameStateBoard();
        board.print(players);


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
