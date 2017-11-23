package UI;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.Players.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        List<Player> players = new ArrayList<Player>();

        players.add(new HumanPlayer());
        players.get(0).setChips(100);
        players.add(new ComputerPlayer());
        players.get(1).setChips(600);

        players.add(new HumanPlayer());
        players.get(2).setChips(400);
        players.add(new ComputerPlayer());
        players.get(3).setChips(200);

        printTwoPlayers(players.subList(0,2));
        printTwoPlayers(players.subList(2,4));


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


    private static void printTwoPlayers(List<Player> players)
    {
        Scanner in=new Scanner(System.in);
        int maxLength=0;

        List<String> list = new ArrayList<String>();
        for(Player p:players )
            list.addAll(p.listOfDetails());

        String s="";
        for (String st:list) {
            if (maxLength<st.length())
                maxLength=st.length();
        }
        printBorder(maxLength,players.size());

        for(int i=0;i<5; i++)
        {
            for (int j=0; j<players.size(); j++)
            {
                System.out.print("| ");
                s=players.get(j).listOfDetails().get(i);
                System.out.print(s);

                for(int k=s.length();k<=maxLength;k++)
                    System.out.print(" ");

                System.out.print("|");
                System.out.print("          ");
            }
            System.out.println();

        }
        printBorder(maxLength,players.size());


    }

    private static void printBorder(int maxLength, int num)
    {
        for (int j=0; j<num; j++)
        {
            System.out.print("+");
            for (int i = 0; i <= maxLength+1 ; i++) {
                System.out.print("-");
            }
            System.out.print("+");
            System.out.print("          ");
        }
        System.out.println();
    }
}
