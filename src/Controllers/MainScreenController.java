package Controllers;

import Engine.GameManager;
import Engine.HandState;
import Engine.Players.PlayerType;
import Engine.Players.PokerPlayer;
import Engine.PokerHand;
import Engine.Winner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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

        playBettingRounds();

        gameManager.saveHandReplayToFile("handReplay.txt");
    }




    public void playBettingRounds()
    {
        HandState state= currHand.getHandState();

        switch (state)
        {

            case GameInit:
            case bettingAfterFlop:
            case bettingAfterTurn:
            case bettingAfterRiver:
            {
                bettingRound();
                break;
            }

            case TheFlop: {
                currHand.dealingFlopCards();
                updateTableCards();
                gameManager.addStepToHandReplay();
                currHand.setHandState(HandState.bettingAfterFlop);
                playBettingRounds();
                break;
            }
            case TheTurn: {
                currHand.dealingTurnCard();
                updateTableCards();
                gameManager.addStepToHandReplay();
                currHand.setHandState(HandState.bettingAfterTurn);
                playBettingRounds();
                break;
            }
            case TheRiver:
            {
                currHand.dealingRiverCard();
                updateTableCards();
                gameManager.addStepToHandReplay();
                currHand.setHandState(HandState.bettingAfterRiver);
                playBettingRounds();
                break;
            }

            case END:
            {
                gameInfoAndActionsController.enableReplayButtons();
                gameInfoAndActionsController.enableBuyButtons();
                gameInfoAndActionsController.enableRunNextHandButton();

                String message=currHand.getWinnersToDisplay();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("We have some winners!");
                alert.setContentText(message);
                alert.showAndWait();

                updateHandReplayWithTheWinners(message);

                break;
            }
        }
    }

    private void updateHandReplayWithTheWinners(String message) {

        currHand.setLastPlayerToPlay(-888);
        currHand.setLastAction(message);
        gameManager.addStepToHandReplay();

    }


    private void bettingRound() {
        PokerPlayer currPlayer = currHand.getNextToPlay();

        gameTableController.BoldCurrPlayer(currPlayer, currHand.getPlayers());
        playersTableController.BoldCurrPlayer(currPlayer);

        if (currPlayer.getType() == Computer) {

            String whatToDo=currPlayer.play();
            System.out.println("Computer player is now playing and he wants to: "+whatToDo);
            currPlayer.setAction(whatToDo);
            currPlayer.setAdditionalActionInfo(1);
            currHand.bettingRoundForAPlayer();
            gameManager.addStepToHandReplay();
            updateGUIPotAndPlayerBetAndChips();
            playBettingRounds();

        }
        else
        {
            System.out.println("Human player");
            if (currPlayer.getPlayerSelection()!="NOT SELECTED")
            {
                currHand.bettingRoundForAPlayer();
                gameManager.addStepToHandReplay();
                updateGUIPotAndPlayerBetAndChips();
                playBettingRounds();

            }
            else
            {
                enableHumanTurnButtons(currPlayer);
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

    private void enableHumanTurnButtons(PokerPlayer currPlayer) {
        gameInfoAndActionsController.enableButtons(currPlayer);
    }

    private void disableHumanTurnButtons() {
        gameInfoAndActionsController.disableHumanButtons();
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
        String playerNameAndID=gameManager.getHandReplay().get(step).getPlayerAndID();
        gameTableController.updateReplayInfo(info,playerNameAndID);

    }

    public void updateCurrPlayerWithSelection(String action, int info) {

        PokerPlayer currPlayer = currHand.getNextToPlay();
        currPlayer.setAction(action);
        currPlayer.setAdditionalActionInfo(info);
        disableHumanTurnButtons();
        playBettingRounds();

    }


}
