package Controllers;

import Engine.GameManager;
import Engine.HandState;
import Engine.Players.PokerPlayer;
import Engine.PokerHand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import static Engine.Players.PlayerType.Computer;

public class MainScreenController implements Initializable {

    @FXML private ScrollPane gameInfoAndActions;
    @FXML private Pane gameTable;
    @FXML private ScrollPane playersTable;
    @FXML private GridPane mainMenu;
    @FXML private GameTableController gameTableController;
    @FXML private GameInfoAndActionsController gameInfoAndActionsController;
    @FXML private MainMenuController mainMenuController;
    @FXML private PlayersTableController playersTableController;

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
        pokerPlayers.addAll(gameManager.getPlayers());

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

    private void updateTableCards() {
        String[] tableCards = gameManager.getCurrHand().getCardsAsStringArray();
        gameTableController.updateCards(tableCards);
    }

    public void updateColor(Color value) {
        gameTableController.changeTableColor(value);
    }

    public void RunOneHand() {

        clearAllCardsOnTable();
        gameManager.startNewHand();
        currHand = gameManager.getCurrHand();
        gameManager.resetPlayerState();
        updateTableCards();

        gameManager.clearHandReplay();

        currHand.dealingHoleCards();
        updateTableCards();

        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();

        currHand.betSmall();
        //currHand.updatePlayersWithEquity();
        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();

        updatePlayersTable();
        updateGUIPotAndPlayerBetAndChips();

        currHand.betBig();
        //currHand.updatePlayersWithEquity();
        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();

        updatePlayersTable();
        updateGUIPotAndPlayerBetAndChips();

        currHand.updateMaxBet();

        playBettingRounds();

    }




    private void playBettingRounds()
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
                gameManager.clearValuesFromCurrHand();

                currHand.setHandState(HandState.bettingAfterFlop);
                playBettingRounds();
                break;
            }
            case TheTurn: {
                currHand.dealingTurnCard();
                updateTableCards();

                gameManager.addStepToHandReplay();
                gameManager.clearValuesFromCurrHand();

                currHand.setHandState(HandState.bettingAfterTurn);
                playBettingRounds();
                break;
            }
            case TheRiver:
            {
                currHand.dealingRiverCard();
                updateTableCards();

                gameManager.addStepToHandReplay();
                gameManager.clearValuesFromCurrHand();

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
                updateHandCount();
                gameManager.saveHandReplayToFile("handReplay.txt");
                break;
            }
        }
    }

    private void updateHandCount() {

        gameInfoAndActionsController.updateHandsCount();

        //We need to start a new game
        if (gameManager.getHandNumber()>=gameManager.getHandsCount())
        {
            mainMenuController.enableLoadXMLButton();
            mainMenuController.enableStartGameButton();
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

        List<String> options= currHand.getPossibleOptions();

        if (currPlayer.getType() == Computer) {


            String whatToDo=currPlayer.getSelection(options);
            currPlayer.setAction(whatToDo);
            int randomNum =  (new Random().nextInt((currHand.getMaxBet())) )+ 1;
            currPlayer.setAdditionalActionInfo(randomNum);
            System.out.println("Computer player ("+currPlayer.getName()+
                    ") is now playing and he wants to: "+whatToDo +" ("+randomNum+")");
            currHand.bettingRoundForAPlayer();
            gameManager.addStepToHandReplay();
            updateGUIPotAndPlayerBetAndChips();
            playBettingRounds();

        }
        else
        {
            System.out.println("Human player");
            if (!Objects.equals(currPlayer.getPlayerSelection(), "NOT SELECTED"))
            {
                currHand.bettingRoundForAPlayer();
                gameManager.addStepToHandReplay();
                updateGUIPotAndPlayerBetAndChips();
                playBettingRounds();

            }
            else
            {
                enableHumanTurnButtons(currPlayer,options,currHand.getMaxBet());
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

    private void enableHumanTurnButtons(PokerPlayer currPlayer, List<String> options, int maxBet) {
        gameInfoAndActionsController.enableButtons(currPlayer,options,maxBet);
    }

    private void disableHumanTurnButtons() {
        gameInfoAndActionsController.disableHumanButtons();
    }


    public void updatePlayersTableFromStep(int step) {
        ObservableList<PokerPlayer> pokerPlayers = FXCollections.observableArrayList();

        pokerPlayers.addAll(gameManager.getHandReplay().get(step).getPlayers());

        playersTableController.updatePlayersTable(pokerPlayers);

    }

    public void updateTableCardsFromStep(int step) {

        String[] tableCards=gameManager.getHandReplay().get(step).getTableCards();
        gameTableController.updateCards(tableCards);
    }

    public void updatePlayersOnTable(int step) {
        gameTableController.clearPlayersEquity();
        List<PokerPlayer> listOfPlayers=gameManager.getHandReplay().get(step).getPlayers();
        gameTableController.updatePlayersOnTable(listOfPlayers);
        gameTableController.updatePlayersEquity(listOfPlayers);
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


    public void updatePlayersCardsOnTableFromStep(int step) {
        List<PokerPlayer> playersList=gameManager.getHandReplay().get(step).getPlayers();
        gameTableController.updatePlayersCards(playersList);
    }

    public void clearAllCardsOnTable() {
        gameTableController.clearPlayersCardsOnTable();
        gameTableController.clearTableCards();
    }

    public void setStyle1() {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().getStylesheets().add(MainScreenController.class.getResource("/Resources/CSS/Style1.css").toExternalForm());
    }

    public void setStyle2() {
        primaryStage.getScene().getStylesheets().clear();
        primaryStage.getScene().getStylesheets().add(MainScreenController.class.getResource("/Resources/CSS/Style2.css").toExternalForm());
    }

    public void removeStyle()
    {
        primaryStage.getScene().getStylesheets().clear();
    }
}
