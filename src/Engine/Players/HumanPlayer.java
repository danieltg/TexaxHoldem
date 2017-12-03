package Engine.Players;

import Engine.PokerHand;


import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(int id)
    {
        super(id);
        setType(PlayerType.Human);

    }
    @Override
    public String play() {
        return PokerHand.getUserSelection();
    }

    @Override
    public int getRaise(int min, int max) {
        Scanner scanner=new Scanner(System.in);
        System.out.print("What would you like to raise to? ");
        return Integer.parseInt(scanner.nextLine());
    }


    public List<String> listOfDetailesForHand()
    {
        List<String> list = super.listOfDetailesForHand();
        list.add("Cards: "+this.getHoleCards());

        return list;

    }
}
