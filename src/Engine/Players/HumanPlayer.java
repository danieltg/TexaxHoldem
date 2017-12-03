package Engine.Players;

import Engine.PokerHand;


import java.util.List;

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


    public List<String> listOfDetailesForHand()
    {
        List<String> list = super.listOfDetailesForHand();
        list.add("Cards: "+this.getHoleCards());

        return list;

    }
}
