package Engine.Players;

public class ComputerPlayer extends Player {

    public ComputerPlayer()
    {
        super();
        setType(PlayerType.Computer);
        setState(PlayerState.DEALER);
    }

    @Override
    public void play() {
        System.out.println("ComputerPlayer-> HumanPlayer");

    }
}
