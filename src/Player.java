

public abstract class Player {
    enum PlayerTypeSign{H,C};
    enum PlayerStateSign{D,S,B,NoState};
    private  PlayerTypeSign playerType=PlayerTypeSign.C;
    private PlayerStateSign PlayerState=PlayerStateSign.NoState;
    private boolean isActive=true;
    private int cheeps=0;
    private Cards[] playerCards=new Cards[2];

    private int handsWon=0;

    public Player(){};

    public void play() {

    }


}
