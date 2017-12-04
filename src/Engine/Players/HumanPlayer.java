package Engine.Players;


import java.util.List;

public class HumanPlayer extends Player {

    public HumanPlayer(int id)
    {
        super(id);
        setType(PlayerType.Human);

    }

    @Override
    public String play() {
        return null;
    }


    @Override
    public int getRaise(int min, int max) {return 0;
    }


    public List<String> listOfDetailesForHand()
    {
        List<String> list = super.listOfDetailesForHand();
        list.add("Cards: "+this.getHoleCards());

        return list;

    }
}
