package Engine;


import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.GameType;
import Engine.Players.*;
import UI.Boards.GameStateBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static Engine.GameDescriptor.GameDescriptor.NUM_OF_COMPUTER_PLAYERS;
import static Engine.GameDescriptor.GameDescriptor.NUM_OF_HUMAN_PLAYERS;

public class GameManager implements Serializable {

    private int numberOfPlayers=NUM_OF_COMPUTER_PLAYERS+NUM_OF_HUMAN_PLAYERS;
    private GameDescriptor gameDescriptor;
    private CurrGameState stateOfGame;
    private List<Player> players= new ArrayList<>();
    private int dealerIndex;
    private int maxPot;
    private Date startTime;
    private int indexActivePlayer;

    private int moneyFromLastHand;

    public static int handNumber;

    public  GameManager(){
        stateOfGame=CurrGameState.NotInitialized;
        moneyFromLastHand=0;
    }

    public int getMoneyFromLastHand() {
        return moneyFromLastHand;
    }

    public void setMoneyFromLastHand(int moneyFromLastHand) {
        this.moneyFromLastHand = moneyFromLastHand;
    }

    public void setGameDescriptor(GameDescriptor gameDescriptor) {
        this.gameDescriptor = gameDescriptor;
        this.stateOfGame=CurrGameState.Initialized;
    }

    public CurrGameState GetStateOfGame()
    {
        return stateOfGame;
    }

    public void buy()
    {
        int numToBuy=gameDescriptor.getStructure().getBuy();
        players.get(indexActivePlayer).buy(numToBuy);
        maxPot+=numToBuy;
    }

    public GameDescriptor getGameDescriptor() {
        return gameDescriptor;
    }

    public void startGame() throws GameStateException {
        switch (stateOfGame)
        {
            case Started:
                throw new GameStateException(GameStateException.INVALID_VALUE + ": The game has already started");

            case NotInitialized:
                throw new GameStateException(GameStateException.INVALID_VALUE + ": Game failed to start, you must have configuration file loaded");

            case Ended:
            case Initialized:
            {
                startTime= new Date();
                stateOfGame = CurrGameState.Started;
                //TODO: remove it!! 0== HUMAN_PLAYER
                indexActivePlayer=0;
                break;
            }

        }

    }


    public void printGameState()
    {
        System.out.println("================================================");
        System.out.println("|      Game State                              |");
        System.out.println("================================================");
        GameStateBoard.printGameState(players);
    }

    public void setRoles(int index) {

        int d=index%numberOfPlayers;
        int s= (d+1)%numberOfPlayers;
        int b=(s+1)%numberOfPlayers;
        int n=(b+1)%numberOfPlayers;

        players.get(d).setState(PlayerState.DEALER);
        players.get(n).setState(PlayerState.NONE);
        players.get(s).setState(PlayerState.SMALL);
        players.get(b).setState(PlayerState.BIG);


        players.get(s).setInitialAmount(gameDescriptor.getStructure().getBlindes().getSmall());
        players.get(b).setInitialAmount(gameDescriptor.getStructure().getBlindes().getBig());
        players.get(d).setInitialAmount(0);
        players.get(n).setInitialAmount(0);

        dealerIndex=d;

    }

    public void setStateOfGame(CurrGameState stateOfGame) {
        this.stateOfGame = stateOfGame;
    }

    private void setPlayers() {

        if (gameDescriptor.getType()== GameType.Basic)
        {

            players.clear();

            for (int i=0; i<NUM_OF_HUMAN_PLAYERS; i++) {
                players.add(new HumanPlayer(i));
                indexActivePlayer=i;
                buy();
            }

            for (int i=NUM_OF_HUMAN_PLAYERS; i<numberOfPlayers; i++) {
                players.add(new ComputerPlayer(i));
                indexActivePlayer=i;
                buy();
            }
       }

    }



    public void getStatistics() {

        long totalTime= (new Date()).getTime()-startTime.getTime();
        long minutes=TimeUnit.MILLISECONDS.toMinutes(totalTime);
        long seconds=TimeUnit.MILLISECONDS.toSeconds(totalTime)- TimeUnit.MINUTES.toSeconds(minutes);

        System.out.println("================================================");
        System.out.println("|      Game Statistics                         |");
        System.out.println("================================================");
        System.out.println("Time: "+ minutes+ ":"+ seconds);
        System.out.println("Hands: "+handNumber+ "/"+this.getGameDescriptor().getStructure().getHandsCount());
        System.out.println("Max pot: "+maxPot);
        printGameState();

    }

    public void setTable()
    {
        int randomNumber= new Random().nextInt(numberOfPlayers);
        maxPot=0;
        handNumber=0;
        setPlayers();
        setRoles(randomNumber);
    }
    public int getBig(){ return gameDescriptor.getStructure().getBlindes().getBig();}
    public int getSmall(){ return gameDescriptor.getStructure().getBlindes().getSmall();}

    public void resetGame() {
        this.stateOfGame=CurrGameState.Initialized;
        setTable();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getDealerIndex() {
        return dealerIndex;
    }

    public boolean doesBigAndSmallPlayersHaveMoney()
    {
        for(Player p: players)
        {
            if (p.getState()==PlayerState.BIG && p.getChips()<getBig())
                return false;

            if (p.getState()==PlayerState.SMALL && p.getChips()<getSmall())
                return false;

        }

        return true;
    }

    public boolean doesHumanPlayersHaveMoney() {

        for (Player p:players)
        {
            if (p.getType()== PlayerType.Human && p.getChips()<=0)
                return false;
        }

        return true;
    }
}

