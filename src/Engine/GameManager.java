package Engine;


import Engine.Exceptions.GameStateException;
import Engine.GameDescriptor.PokerBlindes;
import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.*;
import UI.Boards.GameStateBoard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static Engine.Utils.EngineUtils.saveListToFile;

public class GameManager implements Serializable {

    private int numberOfPlayers;
    private PokerGameDescriptor gameDescriptor;
    private CurrGameState stateOfGame;
    private List<PokerPlayer> players= new ArrayList<>();
    private int dealerIndex;
    private int maxPot;
    private Date startTime;
    private PokerHand currHand;
    private List<PokerHandStep> handReplay=new ArrayList<>();

    private int moneyFromLastHand;

    public static int handNumber;

    public  GameManager(){
        stateOfGame=CurrGameState.NotInitialized;
        moneyFromLastHand=0;
        numberOfPlayers=0;
    }

    public int getHandNumber() {
        return handNumber;
    }

    public int getMoneyFromLastHand() {
        return moneyFromLastHand;
    }

    public void setMoneyFromLastHand(int moneyFromLastHand) {
        this.moneyFromLastHand = moneyFromLastHand;
    }

    public void setGameDescriptor(PokerGameDescriptor gameDescriptor) {
        this.gameDescriptor = gameDescriptor;
        this.numberOfPlayers=this.gameDescriptor.getNumberOfPlayers();
        this.stateOfGame=CurrGameState.Initialized;
    }

    public CurrGameState GetStateOfGame()
    {
        return stateOfGame;
    }

    public void buy(PokerPlayer p)
    {
        int numToBuy=gameDescriptor.getStructure().getBuy();
        p.buy(numToBuy);
        maxPot+=numToBuy;
    }

    public PokerGameDescriptor getGameDescriptor() {
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

        players.get(d).setState(PlayerState.DEALER);
        players.get(s).setState(PlayerState.SMALL);
        players.get(b).setState(PlayerState.BIG);

        players.get(d).setStyle("-fx-background-color: red;");
        players.get(s).setStyle("-fx-background-color: green;");
        players.get(b).setStyle("-fx-background-color: yellow;");

        players.get(s).setInitialAmount(gameDescriptor.getStructure().getBlindes().getSmall());
        players.get(b).setInitialAmount(gameDescriptor.getStructure().getBlindes().getBig());
        players.get(d).setInitialAmount(0);

        dealerIndex=d;

    }

    public void setStateOfGame(CurrGameState stateOfGame) {
        this.stateOfGame = stateOfGame;
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

    public int getMaxPot() {return maxPot;}

    public void setTable()
    {
        players.clear();
        int randomNumber= new Random().nextInt(numberOfPlayers);
        maxPot=0;
        handNumber=0;
        players=gameDescriptor.getPlayers();
        for (PokerPlayer p: players)
            buy(p);

        setRoles(randomNumber);
    }
    public int getBig(){ return gameDescriptor.getStructure().getBlindes().getBig();}
    public int getSmall(){ return gameDescriptor.getStructure().getBlindes().getSmall();}

    public int getBuy(){ return gameDescriptor.getStructure().getBuy();}
    public int getHandsCount() {return gameDescriptor.getStructure().getHandsCount();}

    public boolean getIsFixed(){return gameDescriptor.getStructure().getBlindes().isFixed();}
    public int getAdditions(){return gameDescriptor.getStructure().getBlindes().getAdditions();}

    public void resetGame() {
        this.stateOfGame=CurrGameState.Initialized;
        setTable();
    }

    public List<PokerPlayer> getPlayers() {
        return players;
    }

    public int getDealerIndex() {
        return dealerIndex;
    }

    public boolean doesBigAndSmallPlayersHaveMoney()
    {
        for(PokerPlayer p: players)
        {
            if (p.getState()==PlayerState.BIG && p.getChips()<getBig())
                return false;

            if (p.getState()==PlayerState.SMALL && p.getChips()<getSmall())
                return false;

        }

        return true;
    }

    public boolean doesHumanPlayersHaveMoney() {

        for (PokerPlayer p:players)
        {
            if (p.getType()== PlayerType.Human && p.getChips()<=0)
                return false;
        }

        return true;
    }

    public PokerHand startNewHand()
    {
        handNumber++;
        PokerBlindes blindes=getGameDescriptor().getStructure().getBlindes();

        currHand= new PokerHand(blindes,getPlayers());
        currHand.addToPot(getMoneyFromLastHand());
        currHand.setHandState(HandState.GameInit);
        return currHand;
    }

    public void runHand()
    {
        handReplay.clear();
        resetPlayerState();

        currHand.dealingHoleCards();
        addStepToHandReplay();
        clearValuesFromCurrHand();

        currHand.dealingFlopCards();
        addStepToHandReplay();
        clearValuesFromCurrHand();
    }


    public void resetPlayerState()
    {
        currHand.resetPlayersBets();
        currHand.setLastRaise(null);
    }

    public void addStepToHandReplay() {


        PokerHandStep step= new PokerHandStep(
                currHand.getPlayers(),
                currHand.getLastPlayerToPlay(),
                currHand.getPot(),
                currHand.getCardsAsStringArray(),
                currHand.getCurrentBet(),
                currHand.getLastAction()
                ,currHand.getLastActionInfo());

        handReplay.add(step);
    }


    public void clearValuesFromCurrHand() {

        currHand.setLastPlayerToPlay(-999);
        currHand.setLastActionInfo(0);
        currHand.setLastAction("N");
    }

    public PokerHand getCurrHand()
    {
        return currHand;
    }

    public void clearHandReplay() {
        handReplay.clear();
    }


    public void saveHandReplayToFile(String s) {
        saveListToFile(handReplay,"handReplay.txt");

    }

    public List<PokerHandStep> getHandReplay()
    {
        return handReplay;
    }


}

