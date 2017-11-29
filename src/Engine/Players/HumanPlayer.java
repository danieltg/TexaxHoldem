package Engine.Players;

import Engine.Hand;
import UI.Boards.GameStateBoard;
import UI.Menus.HandMenu;

import java.util.List;

public class HumanPlayer extends Player {

    public HumanPlayer(int id)
    {
        super(id);
        setType(PlayerType.Human);

    }
    @Override
    public String play() {
        return Hand.getUserSelection();
    }


    public List<String> listOfDetailesForHand()
    {
        List<String> list = super.listOfDetailesForHand();
        list.add("Cards: "+this.getHoleCards());

        return list;

    }
}
