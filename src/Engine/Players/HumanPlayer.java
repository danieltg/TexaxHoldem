package Engine.Players;

import java.util.List;

public class HumanPlayer extends Player {

    public HumanPlayer(int id)
    {
        super(id);
        setType(PlayerType.Human);

    }
    @Override
    public void play() {
        System.out.println("Play-> HumanPlayer");
    }


    public List<String> listOfDetailesForHand()
    {
        List<String> list = super.listOfDetailesForHand();
        list.add("Cards: "+this.getHoleCards());

        return list;

    }
}
