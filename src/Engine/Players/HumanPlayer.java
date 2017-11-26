package Engine.Players;

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
}
