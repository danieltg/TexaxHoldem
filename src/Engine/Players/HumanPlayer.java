package Engine.Players;

public class HumanPlayer extends Player {

    public HumanPlayer()
    {
        super();
        setType(PlayerType.Human);
        setState(PlayerState.BIG);

    }
    @Override
    public void play() {
        System.out.println("Play-> HumanPlayer");
    }
}
