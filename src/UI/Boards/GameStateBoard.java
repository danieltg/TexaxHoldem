package UI.Boards;

import Engine.Players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameStateBoard {

  public static void print ( List<Player> players)
  {
      List<Player> tmp = new ArrayList<Player>();
      tmp.add(players.get(0));
      tmp.add(players.get(3));

      printTwoPlayers(tmp);

      tmp.clear();
      tmp.add(players.get(1));
      tmp.add(players.get(2));
      printTwoPlayers(tmp);
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
                System.out.print("       ");
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
            System.out.print("       ");
        }
        System.out.println();
    }
}
