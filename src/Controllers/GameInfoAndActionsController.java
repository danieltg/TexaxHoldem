package Controllers;

import Engine.GameManager;
import Engine.Players.PokerPlayer;
import Engine.PokerHandStep;
import Engine.Utils.EngineUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.beans.binding.Bindings.not;

public class GameInfoAndActionsController implements Initializable{

    @FXML private Button buyButton;
    @FXML private ChoiceBox<String> dropDownPlayers;
    @FXML private Label replayIndex;
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
    @FXML private Button  okRaiseButton;
    @FXML private Button  okBetButton;
    @FXML private TextField raiseAmountLabel;
    @FXML private TextField betAmountLabel;

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
    private String selection;
    private String userSelction;

    private int selectedPlayer = -1;

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

        buyButton.setDisable(true);
        buyButton.setOnAction(event -> {
            if (selectedPlayer!=-1) {
                gameManager.buy(gameManager.getPlayers().get(selectedPlayer));
                businessLogic.updatePlayersList();
                businessLogic.updateGUIPotAndPlayerBetAndChips();
            }
        });

        dropDownPlayers.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
            System.out.println("New Selected Option: " +newValue.toString());
            selectedPlayer= Integer.parseInt(newValue.toString());
        });
        dropDownPlayers.setDisable(true);

        firstCardImage.setImage(new Image(EngineUtils.BASE_PACKAGE+"back.png"));
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
        raiseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="R";
            }
        });

        checkButton.setDisable(true);
        checkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="K";
            }
        });

        betButton.setDisable(true);
        betButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="B";
            }
        });

        callButton.setDisable(true);
        callButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="C";
            }
        });

        foldButton.setDisable(true);
        foldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selection="F";
            }
        });


        gameSettings.setCollapsible(false);
        gameDetails.setCollapsible(false);

        showCardsButton.setDisable(true);
        runNextHandButton.setDisable(true);
        replayButton.setDisable(true);
        replayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startReplay();
            }
        });

        prevButton.setDisable(true);
        prevButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                step--;

                if (step==0)
                {
                    prevButton.setDisable(true);
                }

                nextButton.setDisable(false);
                replayIndex.setText(String.valueOf(step));
                updateGUIWithStep();

            }
        });

        nextButton.setDisable(true);
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                step++;

                if (step>=replay.size())
                {
                    nextButton.setDisable(true);
                    step--;
                }
                
                updateGUIWithStep();
                prevButton.setDisable(false);
                replayIndex.setText(String.valueOf(step));

            }
        });

        betAmountLabel.setVisible(false);
        raiseAmountLabel.setVisible(false);
        okBetButton.setVisible(false);
        okRaiseButton.setVisible(false);

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

    private void updateGUIWithStep() {

        businessLogic.updateGUIwithStep(step);
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

        handsCount.set(gameManager.handNumber);
        maxPOT.set(gameManager.getMaxPot());
        updateDropDownPlayersList();
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }


    public String getUserSelection()
    {
        //selection=null;
        raiseButton.setDisable(false);
        checkButton.setDisable(false);
        betButton.setDisable(false);
        callButton.setDisable(false);
        foldButton.setDisable(false);


        return userSelction;
    }

    public void userSeletedCheck(ActionEvent actionEvent) {
        userSelction="C";
        
    }

    public void enableButtons(PokerPlayer currPlayer) {

        this.currPlayer=currPlayer;

        raiseButton.setDisable(false);
        checkButton.setDisable(false);
        betButton.setDisable(false);
        callButton.setDisable(false);
        foldButton.setDisable(false);

        showCardsButton.setDisable(false);
        firstCardImage.setVisible(true);
        secondCardImage.setVisible(true);

    }


    public void enableReplayButtons()
    {
        replayButton.setDisable(false);
    }

    public void startReplay()
    {
        replay= gameManager.getHandReplay();
        updateGUIWithStep();
        if (step==0)
        {
            replayIndex.setText(String.valueOf(replay.size()));
            prevButton.setDisable(true);
            nextButton.setDisable(false);
        }

    }

    public void setBusinessLogic(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
    }

    public void updateDropDownPlayersList()
    {

        for (PokerPlayer p: gameManager.getPlayers())
        {
            dropDownPlayers.getItems().addAll(p.getName()+" (ID:"+p.getId()+")");
        }


    }

    public void enableBuyButtons() {
        buyButton.setDisable(false);
        dropDownPlayers.setDisable(false);
    }

    public void enableRunNextHand() {
        runNextHandButton.setDisable(true);
    }

    public void runNextHandClicked(ActionEvent actionEvent) {
        businessLogic.runNextHand();
    }
}
