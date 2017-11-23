package UI.Boards;

import Engine.Players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameStateBoard {

  public void print ( List<Player> players)
  {
      printTwoPlayers(players.subList(0,2));
      printTwoPlayers(players.subList(2,4));
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
