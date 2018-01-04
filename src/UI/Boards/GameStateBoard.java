package UI.Boards;

import Engine.Players.PokerPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameStateBoard {

    public static void printHandState(List<PokerPlayer> players,String str)
    {
        int maxLength=getMaxLength(players,"HandState");

        List<PokerPlayer> tmp = new ArrayList<>();
        tmp.add(players.get(0));
        tmp.add(players.get(3));

        printTwoPlayersForHand(tmp,maxLength);

        System.out.println();
        System.out.println("   "+str);
        System.out.println();

        tmp.clear();
        tmp.add(players.get(1));
        tmp.add(players.get(2));
        printTwoPlayersForHand(tmp,maxLength);

    }


    public static int getMaxLength(List<PokerPlayer> players,String str)
    {

        int maxLength=0;

        List<String> list = new ArrayList<>();
        for(PokerPlayer p:players ) {
            if (str.equals("GameSate"))
                list.addAll(p.listOfDetails());
            else
                list.addAll(p.listOfDetailesForHand());
        }


        for (String st:list) {
            if (maxLength<st.length())
                maxLength=st.length();
        }

        return maxLength;
    }
  public static void printGameState ( List<PokerPlayer> players)
  {
      int maxLength=getMaxLength(players,"GameSate");

      List<PokerPlayer> tmp = new ArrayList<>();
      tmp.add(players.get(0));
      tmp.add(players.get(3));

      printTwoPlayers(tmp,maxLength);

      tmp.clear();
      tmp.add(players.get(1));
      tmp.add(players.get(2));
      printTwoPlayers(tmp,maxLength);
  }
    private static void printTwoPlayers(List<PokerPlayer> players, int maxLength)
    {
        String s;
        printBorder(maxLength,players.size());

        for(int i=0;i<6; i++)
        {
            for (PokerPlayer player : players) {
                System.out.print("| ");
                try {
                    s = player.listOfDetails().get(i);

                } catch (Exception e) {
                    s = "";
                }

                System.out.print(s);
                for (int k = s.length(); k <= maxLength; k++)
                    System.out.print(" ");

                System.out.print("|");
                System.out.print("        ");
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
            System.out.print("        ");
        }
        System.out.println();
    }


    private static void printTwoPlayersForHand(List<PokerPlayer> players, int maxLength)
    {
        printBorder(maxLength,players.size());
        String s;
        for(int i=0;i<6; i++)
        {
            for (PokerPlayer player : players) {
                System.out.print("| ");
                try {
                    s = player.listOfDetailesForHand().get(i);

                } catch (Exception e) {
                    s = "";
                }

                System.out.print(s);
                for (int k = s.length(); k <= maxLength; k++)
                    System.out.print(" ");

                System.out.print("|");
                System.out.print("        ");
            }
            System.out.println();

        }
        printBorder(maxLength,players.size());


    }
}
