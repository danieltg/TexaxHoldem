package Engine.Players;

import java.util.Random;

public class ComputerPlayer extends Player {

    public ComputerPlayer(int id)
    {
        super(id);
        setType(PlayerType.Computer);
    }

    @Override
    public String play() {

        int randomNumber= new Random().nextInt(50);

        //10/50 chance of Fold
        //15/50 chance of Raise
        //20/50 chance of Call
        //5/50 chance of Check


        if(randomNumber<5)
            return "F";
        else if (randomNumber<15)
            return "R";
        else if (randomNumber<45)
            return "C";
        else
            return "K";

    }

    @Override
    public int getRaise(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
