package Controllers;

import Engine.GameManager;
import Engine.HandState;
import Engine.Players.PlayerState;
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
    private boolean isFirstTime=true;

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


    public void startNewHand()
    {
        gameManager.startNewHand();
    }

    public void RunOneHand() {

        int nextDeallerIndex = 0;
        isFirstTime=true;

        if(gameManager.getHandNumber()>1) {
            int i = 0;
            for (PokerPlayer p : gameManager.getPlayers()) {
                if (p.getState() == PlayerState.SMALL)
                    nextDeallerIndex=i;
                i++;
            }

            //reset players-- will not reset the State if it's the first round
            gameManager.resetPlayerState();

            gameManager.setRoles(nextDeallerIndex);
        }

        if (gameManager.getIsFixed()==false &&
                ((gameManager.getHandNumber()%(gameManager.getNumberOfPlayers()+1))==0))
        {
            gameManager.updateBigAndSmall();
            businessLogic.updateGameSettings();
        }

        clearAllCardsOnTable();
        businessLogic.clearGameTable();

        //get the hand
        currHand = gameManager.getCurrHand();
        currHand.setSmall(gameManager.getSmall());
        currHand.setBig(gameManager.getBig());
        updateTableCards();

        gameManager.clearHandReplay();

        currHand.dealingHoleCards();
        updateTableCards();

        if (mainMenuController.getEquity())
            currHand.updatePlayersWithEquity();

        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();

        currHand.updateStateIndex();
        currHand.betSmall();

        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();

        updatePlayersTable();
        updateGUIPotAndPlayerBetAndChips();

        currHand.betBig();

        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();

        currHand.setNextToPlayForTheFirstTime();
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
                if (currHand.getMaxBet()==0) {
                    currHand.afterPlayerAction();
                    playBettingRounds();
                }

                else
                    bettingRound();

                break;
            }

            case TheFlop: {
                currHand.dealingFlopCards();
                afterBettingActions();

                currHand.setHandState(HandState.bettingAfterFlop);
                playBettingRounds();
                break;
            }
            case TheTurn: {
                currHand.dealingTurnCard();
                afterBettingActions();

                currHand.setHandState(HandState.bettingAfterTurn);
                playBettingRounds();
                break;
            }
            case TheRiver: {
                currHand.dealingRiverCard();
                afterBettingActions();

                currHand.setHandState(HandState.bettingAfterRiver);
                playBettingRounds();
                break;
            }

            case END:
            {
                gameInfoAndActionsController.enableBuyButtons();
                gameInfoAndActionsController.enableQuitButtons();
                gameInfoAndActionsController.enableRunNextHandButton();
                gameInfoAndActionsController.enableReplayButtons();

                String message=currHand.getWinnersToDisplay();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("We have some winners!");
                alert.setContentText(message);
                alert.showAndWait();

                updateHandReplayWithTheWinners(message);

                gameManager.saveHandReplayToFile("handReplay.txt");
                gameManager.setMoneyFromLastHand(currHand.getPot()%currHand.getNumberOfWinners());

                alert.setHeaderText("Hand finished");
                alert.setContentText("We are going to clear the game table... You can use the Replay feature to see the previous hand");
                alert.showAndWait();
                gameTableController.hideGameTablePane();
                updatePlayersTable();
                updatePlayersTableWithTheWinner(currHand.getWinnerID());
                updateHandCount();
                break;
            }
        }
    }

    private void updatePlayersTableWithTheWinner(int ID) {
        playersTableController.updateTableWithWinner(ID);
    }

    private void afterBettingActions()
    {
        currHand.resetPlayersBets();
        notifyUserOnZeroBet();
        updateGUIPotAndPlayerBetAndChips();
        updateTableCards();

        gameManager.addStepToHandReplay();
        gameManager.clearValuesFromCurrHand();
        currHand.updateNextToPlay();
    }
    private void updateHandCount() {

        gameInfoAndActionsController.updateHandsCount();

        //We need to start a new game
        if (gameManager.getHandNumber()>=gameManager.getHandsCount())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game is Over");
            alert.setContentText("Start new game for playing");
            alert.showAndWait();
            mainMenuController.enableLoadXMLButton();
            mainMenuController.enableRestartButton();

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
            currHand.bettingRoundForAPlayer(mainMenuController.getEquity());
            gameManager.addStepToHandReplay();
            updateGUIPotAndPlayerBetAndChips();
            playBettingRounds();
        }
        else
        {
            if (!Objects.equals(currPlayer.getPlayerSelection(), "NOT SELECTED"))
            {
                currHand.bettingRoundForAPlayer((mainMenuController.getEquity()));
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

    public void clearGameTable() {
        gameTableController.clearPlayersCardsOnTable();
        gameTableController.clearAllPlayersFromScreen();
        gameTableController.clearTableCards();
    }

    public void hideGameTable() {
        gameTableController.hideGameTablePane();
    }

    public boolean isAnimationEnabled() {
        return mainMenuController.getAnimation();
    }

    public void closeGameInfoAndActions() {
        gameInfoAndActionsController.closeAllScrollPanes();
    }


    public void notifyUserOnZeroBet()
    {
        if (currHand.getMaxBet()==0 && isFirstTime==true)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Max bet is zero");
            alert.setContentText("The max bet is zero. We are going to deal the community cards");
            alert.showAndWait();
            isFirstTime=false;
        }
    }
}


