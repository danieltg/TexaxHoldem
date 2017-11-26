package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.GameDescriptor;
import Engine.GameDescriptor.GameType;
import Engine.Players.ComputerPlayer;
import Engine.Players.HumanPlayer;
import Engine.Players.Player;
import Engine.Players.PlayerState;
import UI.Boards.GameStateBoard;

import java.util.ArrayList;
import java.util.List;

import static Engine.GameDescriptor.GameDescriptor.NUM_OF_COMPUTER_PLAYERS;
import static Engine.GameDescriptor.GameDescriptor.NUM_OF_HUMAN_PLAYERS;

public class GameManager {
    private GameDescriptor gameDescriptor;
    private CurrGameState stateOfGame;
    private Deck deck;
    private List<Card> cards= new ArrayList<Card>();
    private List<Player> players= new ArrayList<Player>();
    private int handNumber;

    public  GameManager(){
        stateOfGame=CurrGameState.NotInitialized;
    }

    public void setGameDescriptor(GameDescriptor gameDescriptor) {
        this.gameDescriptor = gameDescriptor;
        this.stateOfGame=CurrGameState.Initialized;
    }


    public CurrGameState GetStateOfGame()
    {
        return stateOfGame;
    }

    public void buy(Player player, int amount)
    {
        player.buy(amount);
        //TODO: add the amount here also...
        //should it be int? or double
    }

    public GameDescriptor getGameDescriptor() {
        return gameDescriptor;
    }

    public void startGame() throws GameStateException {
        if (stateOfGame != CurrGameState.Initialized)
            throw new GameStateException(GameStateException.INVALID_VALUE + ":Can't start game in this state of game");
        else {
            handNumber=0;
            stateOfGame = CurrGameState.Started;
            deck=new Deck();
            setPlayers();
            setRoles();
            printGameState();
        }
    }


    public void printGameState()
    {
        GameStateBoard.print(players);
    }
    private void setRoles() {

        int numberOfPlayers=NUM_OF_COMPUTER_PLAYERS+NUM_OF_HUMAN_PLAYERS;
        int d=handNumber%numberOfPlayers;
        int s= (d+1)%numberOfPlayers;
        int b=(s+1)%numberOfPlayers;
        int n=(b+1)%numberOfPlayers;

        players.get(d).setState(PlayerState.DEALER);
        players.get(s).setState(PlayerState.SMALL);
        players.get(b).setState(PlayerState.BIG);
        players.get(n).setState(PlayerState.NONE);

    }

    public void setStateOfGame(CurrGameState stateOfGame) {
        this.stateOfGame = stateOfGame;
    }

    private void setPlayers()
    {
        if (gameDescriptor.getType()== GameType.Basic)
        {
            for (int i=0; i<NUM_OF_HUMAN_PLAYERS; i++)
                players.add(new HumanPlayer(i));

            for (int i=NUM_OF_HUMAN_PLAYERS; i<NUM_OF_COMPUTER_PLAYERS+NUM_OF_HUMAN_PLAYERS; i++)
                players.add(new ComputerPlayer(i));
       }
    }

    public void runHand() {
        handNumber++;
        setRoles();
        printGameState();
    }
}
