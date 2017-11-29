package Engine.Players;

public class ComputerPlayer extends Player {

    public ComputerPlayer(int id)
    {
        super(id);
        setType(PlayerType.Computer);
    }

    @Override
    public String play() {
        return "F";

    }
}
