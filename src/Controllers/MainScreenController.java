package Controllers;

import Engine.GameManager;
import Engine.Players.PlayerType;
import Engine.Players.PokerPlayer;
import Engine.PokerHand;
import Engine.Winner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static Engine.Players.PlayerType.Computer;
import static Engine.Players.PlayerType.Human;

public class MainScreenController implements Initializable {
    @FXML
    private GameTableController gameTableController;
    @FXML
    private GameInfoAndActionsController gameInfoAndActionsController;
    @FXML
    private MainMenuController mainMenuController;
    @FXML
    private PlayersTableController playersTableController;


    private final GameManager gameManager = new GameManager();
    private Stage primaryStage;
    private BusinessLogic businessLogic;
    private PokerHand currHand;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainMenuController.setGameManager(gameManager);
        gameInfoAndActionsController.setGameManager(gameManager);
        gameTableController.setGameManager(gameManager);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainMenuController.setPrimaryStage(this.primaryStage);
    }


    public void setBusinessLogic(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
        mainMenuController.setBusinessLogic(this.businessLogic);
        gameTableController.setBusinessLogic(this.businessLogic);
        gameInfoAndActionsController.setBusinessLogic(this.businessLogic);
    }

    public void updatePlayersTable() {
        ObservableList<PokerPlayer> pokerPlayers = FXCollections.observableArrayList();
        for (PokerPlayer p : gameManager.getPlayers())
            pokerPlayers.add(p);

        playersTableController.updatePlayersTable(pokerPlayers);
    }

    public void setGameSettings() {
        gameInfoAndActionsController.updateGameSettings();
    }

    public void updateGameDetails() {
        gameInfoAndActionsController.updateGameDetails();
    }

    public void setGameTable() {
        gameTableController.updatePlayersOnTable(gameManager.getPlayers());
        gameTableController.updatePot(0);
    }

    public void updateTableCards() {
        String[] tableCards = gameManager.getCurrHand().getCardsAsStringArray();
        gameTableController.updateCards(tableCards);
    }

    public void updateColor(Color value) {
        gameTableController.changeTableColor(value);
    }

    public void RunOneHand() {

        gameManager.startNewHand();
        currHand = gameManager.getCurrHand();
        gameManager.resetPlayerState();
        updateTableCards();

        gameManager.clearHandReplay();

        currHand.dealingHoleCards();
        updateTableCards();

        gameManager.addStepToHandReplay();

        currHand.betSmall();
        gameManager.addStepToHandReplay();
        updatePlayersTable();
        updateGUIPotAndPlayerBetAndChips();

        currHand.betBig();
        gameManager.addStepToHandReplay();

        updatePlayersTable();
        updateGUIPotAndPlayerBetAndChips();

        currHand.updateMaxBet();
        currHand.updateMaxBet();
        try {
            List<Winner> winners = runHand();
            gameInfoAndActionsController.enableReplayButtons();
            gameInfoAndActionsController.enableBuyButtons();
            if (gameManager.getHandNumber() <= gameManager.getHandsCount()) {
                gameInfoAndActionsController.enableRunNextHand();
            } else {
                //TODO game is over
            }
            gameManager.saveHandReplayToFile("handReplay.txt");

//            for (Winner w: winners) {
//                w.getPlayer().isAWinner();
//                int chipsToAdd=currHand.getPot()/winners.size();
//                w.getPlayer().addChips(chipsToAdd);
//                System.out.println("Player with ID: "+w.getPlayer().getId() +
//                        " won with this hand: "+w.getHandRank() +
//                        " .Prize: "+(currHand.getPot()/winners.size()) +"$");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  private List<Winner> runCurrHand() throws Exception {

    //    while(gameManager.getCurrHand().getIsFinished()!=true&&gameManager.getCurrHand().getNextToPlay().getType()==Human)
    //  {

    //}

    //}


    private List<Winner> runHand() throws Exception {
        updateFirstLastRaiseAndCurrBet();
        List<Winner> winners=null;
        int round=currHand.getRound();
        while (currHand.getIsFinished() != true) {

            while(round<=4) {


        runRound();


                gameManager.addStepToHandReplay();
                updateGUIPotAndPlayerBetAndChips();

                if (currHand.playersLeft() == 1||currHand.humanIsLeft())
                    return currHand.getWinner();

                currHand.upRound();

                gameManager.resetPlayerState();

                currHand.dealingFlopCards();
                updateTableCards();
            }
        }
        return  winners;
    }

    private void runRound() {
        boolean isFinished=false;

        while(isFinished==false) {
            PokerPlayer currPlayer = currHand.getNextToPlay();

            gameTableController.BoldCurrPlayer(currPlayer, currHand.getPlayers());
            playersTableController.BoldCurrPlayer(currPlayer);
            if (currPlayer.getType() == Computer) {

            } else {
                gameInfoAndActionsController.enableButtons(currHand.getNextToPlay());

            }
        }
    }





    public void updateGUIPotAndPlayerBetAndChips() {
        gameTableController.updatePot(gameManager.getCurrHand().getPot());

        gameTableController.updatePlayersBetAndChips(gameManager.getCurrHand().getPlayers());
    }

    private void updateFirstLastRaiseAndCurrBet()
    {
        if (currHand.getRound()==0) {
            currHand.setLastRaise(currHand.getPlayers().get((2 + currHand.getDealer()) %currHand.getNumberOfPlayers()));
            currHand.setCurrentBet(currHand.getBlinde().getBig());
        }
        else {
            currHand.setCurrentBet(0);
        }
    }
    private void collectBets() {
        int p;
        int currIndex;



        while (true) {

            if (currHand.playersLeft() == 1 ||currHand.getMaxBet()==0) {
                break;
            }
//fake
            p=3;
            currIndex = (p + currHand.getDealer()) % currHand.getNumberOfPlayers();
            PokerPlayer currPlayer =currHand.getPlayers().get(currIndex);
            currPlayer.itIsMyTurn();


            if (currHand.getLastRaise()==currPlayer || currHand.isAllCheckOccurred()) {
                currPlayer.itIsNotMyTurn();
                break;
            }

            if (currHand.getLastRaise() != currPlayer && !currPlayer.getCheckOccurred()) {
                if (!currPlayer.isFolded()
                        && currPlayer.getChips() > 0) {

                    doThis(nowPlay(currPlayer),currPlayer);

                    if(currPlayer.getType()== Human && currPlayer.isFolded()) {
                        currPlayer.setBet(0);
                        return;
                    }

                    currHand.addToPot(currPlayer.getBet());
                    currPlayer.collectBet();

                    currHand.updateMaxBet();

                    //TODO
                    // TO DELETE!!!
                    if(currPlayer.getType()== Human) {
                        humanTurn(currPlayer);
                        //return;
                    }

                } else {
                    if(currPlayer.isFolded()) {
                        currPlayer.setBet(0);
                    }
                }
            }

            currPlayer.setCheckOccurred(true);
            p++;
            currPlayer.itIsNotMyTurn();
            currHand.setLastPlayerToPlay(currPlayer.getId());
            gameManager.addStepToHandReplay();
        }

    }

    private void humanTurn(PokerPlayer currPlayer) {
        enableHumanTurnButtons(currPlayer);
    }




    private void enableHumanTurnButtons(PokerPlayer currPlayer) {
        gameInfoAndActionsController.enableButtons(currPlayer);
    }

    private String nowPlay(PokerPlayer currPlayer) {

        if (currPlayer.getType()==PlayerType.Computer) {
            return currPlayer.play();
        }
        else {
            return "R";
            //TODO
            // return getUSerSelection();
        }

    }

    //TODO
    private String getUSerSelection() {
        return "E";
    }

    private void doThis(String whatToDo, PokerPlayer p) {

        while (true) {

            if (whatToDo.equals("F")) {
                fold(p);
                break;
            }

            if (whatToDo.equals("B")) {
                bet(p);
                break;
            }

            if (whatToDo.equals("C")) {
                call(p);
                break;
            }

            if (whatToDo.equals("R")) {
                raise(p);
                break;
            }

            if (whatToDo.equals("K")) {
                check(p);
                break;
            }

            if (whatToDo.equals("E")) {
                break;
            }
        }
    }

    private void fold(PokerPlayer player)
    {
        player.setFolded(true);
        player.setBet(0);
        currHand.setLastAction("F");
        currHand.setLastActionInfo(0);
    }

    private void bet(PokerPlayer player) {
        int betTO = 0;
        if (currHand.getLastRaise() == null || currHand.getLastRaise().getBet() == 0) {
            //TODO:
            betTO = 10;

            //if (betTO <= currHand.getCurrentBet() || betTO > player.getChips() || betTO > currHand.getMaxBet()) {
            //    throw new NumberFormatException();
            //}

            currHand.setCurrentBet(betTO);
            currHand.setLastRaise(player);
            player.setBet(currHand.getCurrentBet());

            currHand.setLastAction("B");
            currHand.setLastActionInfo(betTO);
        }
    }

    private void call(PokerPlayer player)
    {
        if(currHand.getCurrentBet()>player.getBet())
        {
            currHand.subFromPot(player.getBet());
            player.addChips(player.getBet());
            currHand.updateMaxBet();
            player.setBet(currHand.getCurrentBet());

            currHand.setLastAction("C");
            currHand.setLastActionInfo(currHand.getCurrentBet());
        }
        else  if(player.getType()== Human) {
            //System.out.println("You can't Call now ,please choose other option");
            //TODO:
            //whatToDo=getUSerSelection();
        }
    }

    private void raise(PokerPlayer player)
    {
        currHand.subFromPot(player.getBet());
        player.addChips(player.getBet());
        currHand.updateMaxBet();

        int raiseTo = 0;
        while (raiseTo == 0) {
            try {

                int maybeNewMaxBet= player.getChips()-currHand.getCurrentBet();
                if (maybeNewMaxBet<currHand.getMaxBet())
                    currHand.setMaxBet(maybeNewMaxBet);

                if (player.getType() == Human) {
                    //todo
                    raiseTo = 2;

                } else
                    raiseTo = player.getRaise(1,currHand.getMaxBet());
            } catch (NumberFormatException e) {
/*                System.out.println("Your input is invalid. Raise must be a number between 1 - " +currHand.getMaxBet()+
                        ", please try again.");*/
                raiseTo = 0;
                continue;
            }

            if ( raiseTo > currHand.getMaxBet())
            {
                if (player.getType() == Human) {
                    System.out.println("Your input is not valid. Raise must be a number between 1 - " +currHand.getMaxBet()+
                            ", please try again.");
                }
                else
                {
                    /*whatToDo=p.play();*/
                }
                raiseTo = 0;
            }

        }


        currHand.setCurrentBet(currHand.getCurrentBet()+ raiseTo);
        currHand.setLastRaise(player);

        currHand.setLastAction("R");
        currHand.setLastActionInfo(raiseTo);

        for(PokerPlayer playerInLoop:currHand.getPlayers())
        {
            if(playerInLoop!=player)
                playerInLoop.setCheckOccurred(false);
        }

        player.setBet(currHand.getCurrentBet());
    }

    private void check(PokerPlayer player)
    {
        if(player.getBet()==currHand.getCurrentBet()&&currHand.getRound()>0)
        {
            if(currHand.getLastRaise()==null)
                currHand.setLastRaise(player);
            player.setBet(0);

            currHand.setLastAction("K");
            currHand.setLastActionInfo(0);
        }
/*        else
        {

            if(p.getType()==PlayerType.Human) {
                System.out.println("You can't Check now ,please choose other option");
                whatToDo=getUSerSelection();
            }
            else
                whatToDo=p.play();
        }*/
    }

    public void updatePlayersTableFromStep(int step) {
        ObservableList<PokerPlayer> pokerPlayers = FXCollections.observableArrayList();

        for(PokerPlayer p: gameManager.getHandReplay().get(step).getPlayers())
            pokerPlayers.add(p);

        playersTableController.updatePlayersTable(pokerPlayers);

    }

    public void updateTableCardsFromStep(int step) {

        String[] tableCards=gameManager.getHandReplay().get(step).getTableCards();
        gameTableController.updateCards(tableCards);
    }

    public void updatePlayersOnTable(int step) {
        List<PokerPlayer> listOfPlayers=gameManager.getHandReplay().get(step).getPlayers();
        gameTableController.updatePlayersOnTable(listOfPlayers);
    }

    public void updatePotFromStep(int step) {

        int pot=gameManager.getHandReplay().get(step).getPot();
        gameTableController.updatePot(pot);
    }

    public void updateStepLabel(int step) {
        String info=gameManager.getHandReplay().get(step).getStepToDisplay();
        gameTableController.updateReplayInfo(info);
    }
}
