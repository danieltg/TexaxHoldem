package Controllers;

import Engine.GameManager;
import Engine.Players.PlayerType;
import Engine.Players.PokerPlayer;
import Engine.PokerHandStep;
import Engine.Utils.EngineUtils;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.beans.binding.Bindings.not;

@SuppressWarnings("deprecation")
public class GameInfoAndActionsController implements Initializable{

    @FXML private Button quitGame;
    @FXML private ChoiceBox<String>  humanPlayersList;

    @FXML private ScrollPane gamePane;
    @FXML private TitledPane handFinishedActions;
    @FXML private Button stopReplay;
    @FXML private TitledPane humanTurn;
    @FXML private Button buyButton;
    @FXML private ChoiceBox<String> dropDownPlayers;
    @FXML private Accordion accordionGame;
    @FXML private TitledPane gameSettings;
    @FXML private TitledPane gameDetails;
    @FXML private GridPane gameSettingsGrid;
    @FXML private Label handsLabel;
    @FXML private Label buysLabel;
    @FXML private Label bigLabel;
    @FXML private Label  smallLabel;
    @FXML private CheckBox isFixedCheckBox;
    @FXML private Label additionsLabel;
    @FXML private Label additionsValue;
    @FXML private Label handsCountLabel;
    @FXML private Label totalHandsLabel;
    @FXML private Label maxPotLabel;
    @FXML private ImageView firstCardImage;
    @FXML private ImageView secondCardImage;
    @FXML private Button showCardsButton;
    @FXML private Button runNextHandButton;
    @FXML private Button  replayButton;
    @FXML private Button  prevButton;
    @FXML private Button nextButton;
    @FXML private Button  raiseButton;
    @FXML private Button  checkButton;
    @FXML private Button  betButton;
    @FXML private Button  callButton;
    @FXML private Button  foldButton;


    @FXML private Spinner betSpinner;
    @FXML private Spinner raiseSpinner;

    @FXML private Label maxBetMsgLabel;

    private GameManager gameManager;
    private PokerPlayer currPlayer;
    private List<PokerHandStep> replay=null;
    private int step;
    private BusinessLogic businessLogic;

    private SimpleIntegerProperty big;
    private SimpleIntegerProperty small;
    private SimpleIntegerProperty buy;
    private SimpleIntegerProperty hands;
    private SimpleIntegerProperty additions;
    private SimpleBooleanProperty isFixed;

    private SimpleIntegerProperty handsCount;
    private SimpleIntegerProperty maxPOT;

    private int selectedPlayer = -1;
    private int selectedPlayerForQuit= -1;
    private boolean showCard1=true;
    private boolean showCard2=true;

    public GameInfoAndActionsController()
    {
        big = new SimpleIntegerProperty(0);
        small = new SimpleIntegerProperty(0);
        buy= new SimpleIntegerProperty(0);
        hands= new SimpleIntegerProperty(0);
        additions= new SimpleIntegerProperty(0);
        isFixed= new SimpleBooleanProperty(true);
        handsCount=new SimpleIntegerProperty(1);
        maxPOT=new SimpleIntegerProperty(0);

        step=0;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        quitGame.setDisable(true);
        quitGame.setOnAction(event -> {
            if (selectedPlayerForQuit!=-1) {
                removePlayerFromGame(selectedPlayerForQuit);
            }
        });

        buyButton.setDisable(true);
        buyButton.setOnAction(event -> {
            if (selectedPlayer!=-1) {
                gameManager.buy(gameManager.getPlayers().get(selectedPlayer));
                businessLogic.updatePlayersList();
                businessLogic.updateGUIPotAndPlayerBetAndChips();
                updateMaxPot();
            }
        });

        maxBetMsgLabel.setVisible(false);
        dropDownPlayers.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            System.out.println("New Selected Option: " +newValue.toString());
            selectedPlayer= Integer.parseInt(newValue.toString());
        });
        dropDownPlayers.setDisable(true);


        humanPlayersList.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            System.out.println("New Selected Option: " +newValue.toString());
            selectedPlayerForQuit= Integer.parseInt(newValue.toString());
        });
        humanPlayersList.setDisable(true);

        firstCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+"back.png"));
        firstCardImage.setOnMouseClicked((EventHandler) event -> {
            if (businessLogic.isAnimationEnabled()) {
                if (showCard1) {
                    fadeIn(0,firstCardImage);
                    showCard1 = false;
                } else {
                    fadeOut(firstCardImage);
                    showCard1 = true;
                }
            }
        });

        secondCardImage.setOnMouseClicked((EventHandler) event -> {
            if (businessLogic.isAnimationEnabled()) {
                if (showCard2) {
                    fadeIn(1,secondCardImage);
                    showCard2 = false;
                } else {
                    fadeOut(secondCardImage);
                    showCard2 = true;
                }
            }
        });


        secondCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+"back.png"));
        firstCardImage.setVisible(false);
        secondCardImage.setVisible(false);

        showCardsButton.setDisable(false);
        showCardsButton.setOnMousePressed((event) -> {
            firstCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+currPlayer.getCardsAsStringArray()[0]+".png"));
            secondCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+currPlayer.getCardsAsStringArray()[1]+".png"));
        });
        showCardsButton.setOnMouseReleased((event) -> {
            firstCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+"back.png"));
            secondCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+"back.png"));
        });
        raiseButton.setDisable(true);
        raiseButton.setOnAction(event -> {
            int raiseTo= Integer.parseInt(raiseSpinner.getEditor().getText());
            businessLogic.updateUserSelection("R",raiseTo);
        });

        raiseSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {

            try{
                Integer.parseInt(raiseSpinner.getEditor().getText());
            }
            catch (Exception e)
            {
                raiseSpinner.getEditor().setText("1");
            }

            if (!newValue) {
                raiseSpinner.increment(0); // won't change value, but will commit editor
            }
        });

        betSpinner.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {

            try{
                Integer.parseInt(betSpinner.getEditor().getText());
            }
            catch (Exception e)
            {
                betSpinner.getEditor().setText("1");
            }

            if (!newValue) {
                betSpinner.increment(0); // won't change value, but will commit editor
            }
        });

        checkButton.setDisable(true);
        checkButton.setOnAction(event -> businessLogic.updateUserSelection("K",0));

        betButton.setDisable(true);
        betButton.setOnAction(event -> {
            int betTo= Integer.parseInt(betSpinner.getEditor().getText());
            businessLogic.updateUserSelection("B",betTo);
        });

        callButton.setDisable(true);
        callButton.setOnAction(event -> businessLogic.updateUserSelection("C",0));

        foldButton.setDisable(true);
        foldButton.setOnAction(event -> businessLogic.updateUserSelection("F",0));


        gameSettings.setCollapsible(false);
        gameDetails.setCollapsible(false);
        humanTurn.setCollapsible(false);
        handFinishedActions.setCollapsible(false);

        showCardsButton.setDisable(true);
        runNextHandButton.setDisable(true);
        replayButton.setDisable(true);
        replayButton.setOnAction(event ->
                startReplay());

        prevButton.setDisable(true);
        prevButton.setOnAction(event -> {
            step--;

            if (step==0)
            {
                prevButton.setDisable(true);
            }

            nextButton.setDisable(false);
            stopReplay.setDisable(false);
            updateGUIWithStep();

        });

        nextButton.setDisable(true);
        nextButton.setOnAction(event -> {
            step++;

            if (step==replay.size()-1)
            {
                nextButton.setDisable(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("We have some winners!");
                alert.setContentText(replay.get(step).getAction());
                alert.showAndWait();

                if (gameManager.getHandNumber()>=gameManager.getHandsCount())
                {
                    alert.setHeaderText("Game over");
                    alert.setContentText("Thanks for playing Texas Hold'em!\nYou can either start the same game or load a new one");
                    alert.showAndWait();
                    return;
                }
                step--;
                return;
            }

            updateGUIWithStep();
            prevButton.setDisable(false);
            stopReplay.setDisable(false);
        });

        stopReplay.setDisable(true);
        stopReplay.setOnAction(event -> {
            prevButton.setDisable(true);
            nextButton.setDisable(true);
            replayButton.setDisable(false);

            runNextHandButton.setDisable(gameManager.getHandNumber()>=gameManager.getHandsCount());

            stopReplay.setDisable(true);
            businessLogic.clearAllCardsOnTable();
            businessLogic.hideGameTable();
            enableBuyButtons();
            enableQuitButtons();

        });

        bigLabel.textProperty().bind(big.asString());
        smallLabel.textProperty().bind(small.asString());
        handsLabel.textProperty().bind(hands.asString());
        totalHandsLabel.textProperty().bind(hands.asString());
        handsCountLabel.textProperty().bind(handsCount.asString());

        buysLabel.textProperty().bind(buy.asString());
        additionsValue.textProperty().bind(additions.asString());
        isFixedCheckBox.selectedProperty().bind(isFixed);

        additionsValue.visibleProperty().bind(not(isFixed));
        additionsLabel.visibleProperty().bind(not(isFixed));

        maxPotLabel.textProperty().bind(maxPOT.asString());
    }

    private void fadeOut(ImageView img) {
        Image image1 = new Image(EngineUtils.BASE_PACKAGE+"back.png");
        SequentialTransition transitionForward = createTransition(img, image1);
        transitionForward.play();
    }

    private void fadeIn(int index, ImageView img) {
        Image image1 = new Image(EngineUtils.BASE_PACKAGE+currPlayer.getCardsAsStringArray()[index]+".png");
        SequentialTransition transitionForward = createTransition(img, image1);
        transitionForward.play();
    }

    SequentialTransition createTransition(final ImageView iv, final Image img){
        FadeTransition fadeOutTransition
                = new FadeTransition(Duration.seconds(1), iv);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        fadeOutTransition.setOnFinished(arg0 -> iv.setImage(img));

        FadeTransition fadeInTransition
                = new FadeTransition(Duration.seconds(1), iv);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        SequentialTransition sequentialTransition
                = SequentialTransitionBuilder
                .create()
                .children(fadeOutTransition, fadeInTransition)
                .build();

        return sequentialTransition;
    }

    private void removePlayerFromGame(int selectedPlayerForQuit) {

        gameManager.removeHumanPlayer(selectedPlayerForQuit);
        if (gameManager.getNumberOfHumanPlayers()==0 || gameManager.getNumberOfPlayers()==2)
        {
            //We should exit the game
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game over");

            if (gameManager.getNumberOfPlayers()==2)
            {
                alert.setContentText("Only 2 players remained in the game... See you next time.");
            }
            else
            {
                alert.setContentText("The last human player left the game... See you next time.");
            }
            alert.showAndWait();

            Platform.exit();
        }
        else
        {
            updateDropDownPlayersList();
            updateHumanDropDownPlayersList();

            businessLogic.updatePlayersList();
        }

    }

    private void updateGUIWithStep() {

        businessLogic.updateGUIwithStep(step);
    }
    public void updateBetSpinner(int min,int max)
    {
        SpinnerValueFactory<Integer> spinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(min,max);
        betSpinner.setValueFactory(spinnerValueFactory);
    }
    public void updateRaiseSpinner(int min,int max)
    {
        SpinnerValueFactory<Integer> spinnerValueFactory=new SpinnerValueFactory.IntegerSpinnerValueFactory(min,max);
        raiseSpinner.setValueFactory(spinnerValueFactory);
    }
    public void updateGameSettings()
    {
        gameSettings.setCollapsible(true);

        big.set(gameManager.getBig());
        small.set(gameManager.getSmall());
        hands.set(gameManager.getHandsCount());
        buy.set(gameManager.getBuy());
        additions.set(gameManager.getAdditions());
        isFixed.set(gameManager.getIsFixed());

        gameSettings.setExpanded(true);
    }

    public void updateGameDetails()
    {
        gameDetails.setCollapsible(true);
        updateHandsCount();
        updateMaxPot();
        updateDropDownPlayersList();
        updateHumanDropDownPlayersList();
    }


    public void updateHandsCount()
    {
        handsCount.set(gameManager.getHandNumber());
    }

    public void updateMaxPot()
    {
        maxPOT.set(gameManager.getMaxPot());
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void enableButtons(PokerPlayer currPlayer, List<String> options, int maxBet) {

        disableHumanButtons();

        humanTurn.setCollapsible(true);
        humanTurn.setExpanded(true);

        showCardsButton.setDisable(false);
        firstCardImage.setVisible(true);
        secondCardImage.setVisible(true);

        this.currPlayer=currPlayer;

        if (options.contains("R"))
        {
            raiseButton.setDisable(false);
            raiseSpinner.setDisable(false);
            maxBetMsgLabel.setVisible(true);
            if (maxBet<1)
            {
                System.out.println("We have bug here, max ber is <1 (Raise)");
                maxBet=1;
            }
            updateRaiseSpinner(1,maxBet);
            maxBetMsgLabel.setText("The max raise is: "+String.valueOf(maxBet));
        }

        if (options.contains("C")) {
            callButton.setDisable(false);
        }

        if (options.contains("K")) {
            checkButton.setDisable(false);
        }

        if (options.contains("B")) {
            betButton.setDisable(false);
            betSpinner.setDisable(false);
            maxBetMsgLabel.setVisible(true);
            if (maxBet<1)
            {
                System.out.println("We have bug here, max bet <1 (Bet)");
                maxBet=1;
            }
            updateBetSpinner(1,maxBet);
            maxBetMsgLabel.setText("The max bet is: "+String.valueOf(maxBet));
        }

        if (options.contains("F")) {
            foldButton.setDisable(false);
        }

    }


    public void enableReplayButtons()
    {
        handFinishedActions.setCollapsible(true);
        handFinishedActions.setExpanded(true);
        replayButton.setDisable(false);

    }

    public void startReplay()
    {
        step=0;

        //Disable the buy button first
        buyButton.setDisable(true);
        dropDownPlayers.setDisable(true);

        quitGame.setDisable(true);
        humanPlayersList.setDisable(true);

        runNextHandButton.setDisable(true);

        replay= gameManager.getHandReplay();
        updateGUIWithStep();
        prevButton.setDisable(true);
        nextButton.setDisable(false);
        stopReplay.setDisable(false);

    }

    public void setBusinessLogic(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
    }

    public void updateDropDownPlayersList()
    {

        dropDownPlayers.getItems().clear();
        for (PokerPlayer p: gameManager.getPlayers())
        {
            dropDownPlayers.getItems().addAll(p.getName()+" (ID:"+p.getId()+")");
        }

    }

    public void updateHumanDropDownPlayersList()
    {
        humanPlayersList.getItems().clear();

        for (PokerPlayer p: gameManager.getPlayers())
        {
            if (p.getType()== PlayerType.Human)
            {
                humanPlayersList.getItems().addAll(p.getName()+" (ID:"+p.getId()+")");
            }
        }
    }

    public void enableQuitButtons()
    {
        quitGame.setDisable(false);
        humanPlayersList.setDisable(false);
    }
    public void enableBuyButtons() {
        buyButton.setDisable(false);
        dropDownPlayers.setDisable(false);
    }


    public void runNextHandClicked() {
        businessLogic.clearAllCardsOnTable();
        //businessLogic.clearGameTable();
        businessLogic.startHand();
    }

    public void disableHumanButtons() {


        raiseButton.setDisable(true);
        checkButton.setDisable(true);
        betButton.setDisable(true);
        callButton.setDisable(true);
        foldButton.setDisable(true);

        betSpinner.setDisable(true);
        raiseSpinner.setDisable(true);

        showCardsButton.setDisable(true);
        firstCardImage.setVisible(false);
        secondCardImage.setVisible(false);
    }

    public void enableRunNextHandButton() {
        runNextHandButton.setDisable(gameManager.getHandNumber()>=gameManager.getHandsCount());
    }

    public void betClicked() {
        maxBetMsgLabel.setVisible(false);

    }

    public void raiseClicked() {
        maxBetMsgLabel.setVisible(false);
    }

    public void closeAllScrollPanes() {

        gameSettings.setExpanded(false);
        gameDetails.setExpanded(false);
        humanTurn.setExpanded(false);
        handFinishedActions.setExpanded(false);

        gameSettings.setCollapsible(false);
        gameDetails.setCollapsible(false);
        humanTurn.setCollapsible(false);
        handFinishedActions.setCollapsible(false);

    }
}
